package com.bulpros.eformsgateway.form.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class CommonUtils {

    private static final ObjectMapper JACKSON_OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json().build();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    @SneakyThrows
    public static <T> T clone(final Object object, final Class<T> type) {
        final var objectAsJsonString = getGeneralMapperAcceptsEmpty().writeValueAsString(object);
        return OBJECT_MAPPER.convertValue(objectAsJsonString, type);
    }

    @SneakyThrows
    public static ObjectMapper jacksonMapper() {
        return JACKSON_OBJECT_MAPPER;
    }

    @SneakyThrows
    public static ObjectMapper ObjMapper() {
        return OBJECT_MAPPER;
    }

    @SneakyThrows
    public static ObjectMapper getGeneralMapperAcceptsEmpty() {
        return OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    @NotNull
    public static String getJsonString(HashMap<String, Object> propertiesMap) {
        JSONObject data = new JSONObject();
        data.put("data", propertiesMap);
        return data.toString();
    }

}
