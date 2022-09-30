package com.bulpros.eforms.processengine.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonUtils {

    public static final String XML = "xml";
    public static final String JSON = "json";
    public static final String PDF = "pdf";
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

    @SneakyThrows
    public static String mapXmlStringFromJsonNode(final JsonNode json, final String replaceThis, String WithThis) {
        ObjectMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        return xmlMapper.writer()
                .with(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
                .writeValueAsString(json)
                .replace(replaceThis, WithThis);
    }
}
