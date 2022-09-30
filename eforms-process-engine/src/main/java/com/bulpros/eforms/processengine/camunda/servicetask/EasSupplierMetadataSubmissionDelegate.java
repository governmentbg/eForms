package com.bulpros.eforms.processengine.camunda.servicetask;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.util.DataUtil;
import com.bulpros.formio.service.SubmissionService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Setter
@Getter
@RequiredArgsConstructor
public class EasSupplierMetadataSubmissionDelegate implements JavaDelegate {

    private static final String ADMINISTRATIVE_UNIT_LIST = "administrativeUnitsList";
    private static final String CHANNELS_AND_TERMS_LIST = "channelsAndTermsList";

    private final AuthenticationFacade authenticationFacade;
    private final SubmissionService submissionService;
    private final ConfigurationProperties processConfProperties;

    private Expression project;
    private Expression submissionId;
    private Expression administrativeUnitsListFormAlias;
    private Expression channelsAndTermsListFormAlias;

    private final Gson gson = new Gson();

    @Override
    public void execute(DelegateExecution delegateExecution) {
        validateParams();

        Map<String, AdministrativeUnit> administrativeUnitMap = new HashMap<>();

        String administrativeUnitFormAlias = (String) this.getAdministrativeUnitsListFormAlias().getValue(delegateExecution);
        JsonArray administrativeUnitArray = getData(delegateExecution, administrativeUnitFormAlias, ADMINISTRATIVE_UNIT_LIST);

        String channelsAndTermsFormAlias = (String) this.getChannelsAndTermsListFormAlias().getValue(delegateExecution);
        JsonArray channelsAndTermsListArray = getData(delegateExecution, channelsAndTermsFormAlias, CHANNELS_AND_TERMS_LIST);

        addAdministrativeUnits(administrativeUnitMap, administrativeUnitArray, Optional.empty());
        addChannelAndTerms(administrativeUnitMap, channelsAndTermsListArray);

        String project = (String) this.getProject().getValue(delegateExecution);
        String form = processConfProperties.getEasSuppliersResourceName();
        ResourcePath path = new ResourcePath(project, form);

        Authentication authentication = this.authenticationFacade.getAuthentication();
        String submissionIdValue = (String) this.getSubmissionId().getValue(delegateExecution);

        JSONArray array = new JSONArray();
        JSONObject payload = DataUtil.getJsonObjectForPatch("replace", "/data/" + ADMINISTRATIVE_UNIT_LIST, administrativeUnitMap.values());
        array.add(payload);

        this.submissionService.updateSubmission(path, authentication, submissionIdValue, array.toJSONString());
    }

    private JsonArray getData(DelegateExecution delegateExecution, String formAlias, String key) {
        Map<String, Object> submissionData = getSubmissionData(delegateExecution, formAlias);
        String data = gson.toJson(submissionData.get(key));
        return getAsJsonArray(data);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getSubmissionData(DelegateExecution execution, String formKey) {
        String submissionDataName = ProcessConstants.SUBMISSION_DATA + EFormsUtils.getFormDataSubmissionKey(formKey);
        Optional<Object> optionalSubmissionData = Optional.ofNullable(execution.getVariable(submissionDataName));
        return (Map<String, Object>) optionalSubmissionData
                .map(map -> ((Map<String, Object>) map).get("data"))
                .orElseThrow(() -> new IllegalArgumentException("Submission data is not present"));
    }

    private JsonArray getAsJsonArray(String jsonString) {
        JsonElement jsonEl = JsonParser.parseString(jsonString);
        return jsonEl != null && jsonEl.isJsonArray() ?
                jsonEl.getAsJsonArray() :
                new JsonArray();
    }

    private void addAdministrativeUnits(Map<String, AdministrativeUnit> administrativeUnitMap, JsonArray administrativeUnitArray, Optional<JsonElement> optionalTaxAndTermEl) {
        for (JsonElement unitEl : administrativeUnitArray) {
            AdministrativeUnit unit = gson.fromJson(unitEl, AdministrativeUnit.class);
            boolean hasUnit = administrativeUnitMap.containsKey(unit.getAdministrationUnitEDelivery().toString());
            if (!hasUnit) {
                unit.setChannelsAndTermsList(new ArrayList<>());
                administrativeUnitMap.put(unit.getAdministrationUnitEDelivery().toString(), unit);
            }

            optionalTaxAndTermEl.ifPresent(taxAndTermEl -> {
                AdministrativeUnit newUnit = administrativeUnitMap.get(unit.getAdministrationUnitEDelivery().toString());
                ChannelsAndTermsList termListItem = gson.fromJson(taxAndTermEl, ChannelsAndTermsList.class);
                newUnit.getChannelsAndTermsList().add(termListItem);
            });
        }
    }

    private void addChannelAndTerms(Map<String, AdministrativeUnit> administrativeUnitMap, JsonArray channelsAndTermsListArray) {
        for (JsonElement taxAndTermEl : channelsAndTermsListArray) {
            JsonArray unitArr = taxAndTermEl.getAsJsonObject().get(ADMINISTRATIVE_UNIT_LIST).getAsJsonArray();
            addAdministrativeUnits(administrativeUnitMap, unitArr, Optional.of(taxAndTermEl));
        }
    }

    private void validateParams() {
        if (this.submissionId == null || this.getSubmissionId() == null) {
            throw new IllegalArgumentException("Submission id not present");
        }

        if (this.administrativeUnitsListFormAlias == null || this.getAdministrativeUnitsListFormAlias() == null) {
            throw new IllegalArgumentException("Administrative Unit form alias is not present");
        }

        if (this.channelsAndTermsListFormAlias == null || this.getChannelsAndTermsListFormAlias() == null) {
            throw new IllegalArgumentException("Channel And Term List form alias id not present");
        }
    }


    @Data
    private static class AdministrativeUnit {
        private Object administrationUnitEDelivery;
        private Object administrationUnitID;
        private Object administrationUnit;
        private List<ChannelsAndTermsList> channelsAndTermsList;
    }

    @Data
    private static class ChannelsAndTermsList {
        private Object deadlineTypeLabel;
        private Object deadlineTerm;
        private Object hasPayment;
        private Object deadlineUnit;
        private Object deadlineType;
        private Object channel;
        private Object validFrom;
        private Object validTo;
        private Object taxAmount;
        private Object currency;
    }
}
