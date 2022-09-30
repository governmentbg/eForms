package com.bulpros.formio.repository.util;

import net.minidev.json.JSONObject;

public class DataUtil {
    public static JSONObject getJsonObjectForPatch(String operation, String pathToObject, Object value) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("op", operation);
        jsonObject.put("path", pathToObject);
        jsonObject.put("value", value);
        return jsonObject;
    }
}
