package com.bulpros.eforms.processengine.camunda.listener;

import com.bulpros.eforms.processengine.camunda.model.ProcessConstants;
import com.bulpros.eforms.processengine.camunda.util.EFormsUtils;
import com.bulpros.eforms.processengine.configuration.ConfigurationProperties;
import com.bulpros.eforms.processengine.security.AuthenticationFacade;
import com.bulpros.formio.repository.formio.ResourcePath;
import com.bulpros.formio.repository.formio.SubmissionFilter;
import com.bulpros.formio.repository.formio.SubmissionFilterClauseEnum;
import com.bulpros.formio.service.SubmissionService;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.internal.JsonContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.spin.Spin;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateTermAndTaxesListener  implements TaskListener {
    private final ConfigurationProperties configuration;
    private final AuthenticationFacade authenticationFacade;
    private final SubmissionService submissionService;
    private final FormService formService;


    @Override
    public void notify(DelegateTask delegateTask) {
        final Authentication authentication = this.authenticationFacade.getAuthentication();
        final TaskFormData taskFormData = this.formService.getTaskFormData(delegateTask.getId());
        final String formKey = taskFormData.getFormKey();

        Map<String,Object> processContext = (Map<String, Object>) delegateTask.getVariable(ProcessConstants.CONTEXT);
        Configuration pathConfiguration = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
        String projectId = JsonPath.using(pathConfiguration).parse(processContext).read("$.formioBaseProject");
        String arId = JsonPath.using(pathConfiguration).parse(processContext)
                .read("$.service.data.arId");
        String supplierId = JsonPath.using(pathConfiguration).parse(processContext)
                .read("$.serviceSupplier.data.code");

        String formDataSubmissionKey = EFormsUtils.getFormDataSubmissionKey(formKey);
        JacksonJsonNode submissionData = (JacksonJsonNode)Spin.JSON(delegateTask.getVariable(ProcessConstants.SUBMISSION_DATA + formDataSubmissionKey));
        JsonContext jsonContext = (JsonContext) JsonPath.parse(submissionData.toString());

        try {
            String channelType = (String)((JSONArray) jsonContext.read(configuration.getFormChannelTypeJsonPathQuery())).get(0);
            String serviceType = (String)((JSONArray) jsonContext.read(configuration.getDeadlineTypeJsonPathQuery())).get(0);

            SubmissionFilter submissionFilter = new SubmissionFilter(SubmissionFilterClauseEnum.NONE,
                    Map.of(
                            configuration.getArIdPropertyKey(), arId,
                            configuration.getServiceSupplierPropertyKey(), supplierId,
                            configuration.getServiceChannelTypePropertyKey(), channelType,
                            configuration.getServiceTypePropertyKey(), serviceType
                    )
            );

            var submission = submissionService.existsWithFilter(
                    new ResourcePath(projectId, configuration.getEasTermAndTaxesResourceName()),
                            authentication, List.of(submissionFilter));
            log.info("Submission with id: " + submission.get_id() + " exists.");
        } catch(Exception exception) {
            log.warn("Channel and service type are not found for service: " + arId + " Reason: " + exception.getMessage());
            throw new ProcessEngineException("CHANNEL_AND_SERVICE_TYPE_NOT_FOUND");
        }
    }

}
