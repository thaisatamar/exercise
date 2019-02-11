package com.thais.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    final static ObjectMapper MAPPER = new ObjectMapper();

    public static String toJ(Object pojo) {
        try {
            return MAPPER.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public static <T> T fromJ(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
