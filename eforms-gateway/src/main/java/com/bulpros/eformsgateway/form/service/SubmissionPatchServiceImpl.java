package com.bulpros.eformsgateway.form.service;

import com.bulpros.formio.repository.util.DataUtil;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class SubmissionPatchServiceImpl implements SubmissionPatchService {


    @Override
    public String createPatchData(HashMap<String, Object> payload) {
        JSONArray jsonArray = new JSONArray();
        payload.entrySet().stream().forEach(entry -> {
            JSONObject newObject = DataUtil.getJsonObjectForPatch(
                    "add", "/data/" + entry.getKey(), entry.getValue());
            jsonArray.add(newObject);
        });
        return jsonArray.toString();
    }
}
