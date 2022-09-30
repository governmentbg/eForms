package com.bulpros.eforms.processengine.camunda.service;

import com.bulpros.formio.dto.ResourceDto;
import com.bulpros.formio.repository.formio.ValueTypeEnum;
import com.bulpros.formio.repository.util.DataUtil;
import com.bulpros.formio.service.SubmissionService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jvnet.hk2.annotations.Service;
import org.springframework.security.core.Authentication;

@Slf4j
@Service
@AllArgsConstructor
public class CreateAndMergeSubmissionImpl<T> implements CreateAndMergeSubmission<T> {

    private final SubmissionService submissionService;
    public void mergeSubmissions(String projectId, String resourceName, ResourceDto submission,
                                    T details, String[] removeFields, Authentication authentication) {
        var id = submission.get_id();
        var jsonPatchArray = new JSONArray();
        var gson = new Gson();

        String detailsJsonString = gson.toJson(details);
        JSONParser parser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
        JSONObject parsedData = null;
        try {
            parsedData = (JSONObject) parser.parse(detailsJsonString);
        } catch (ParseException e) {
            log.error("Could not parse object: " + detailsJsonString);
        }
        for (String field : removeFields) {
            parsedData.remove(field);
        }
        parsedData.forEach((key, value) -> {
            var patchDataObject = DataUtil.getJsonObjectForPatch(
                    "add",
                    "/data/" + key,
                    value
            );
            jsonPatchArray.add(patchDataObject);
        });
        this.submissionService.updateSubmission(
                projectId,
                ValueTypeEnum.ID,
                resourceName,
                ValueTypeEnum.PATH,
                authentication,
                id,
                jsonPatchArray.toJSONString()
        );
    }

    public void createSubmission(String projectId, String resourceName, T details, Authentication authentication) {
        var data = new JSONObject();
        data.put("data", details);
        this.submissionService.createSubmission(
                projectId,
                ValueTypeEnum.ID,
                resourceName,
                ValueTypeEnum.PATH,
                authentication,
                data.toJSONString()
        );
    }
}
