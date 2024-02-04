package com.example.kafkastreamservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class Util {
    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json()
                .failOnEmptyBeans(false)
                .failOnUnknownProperties(false)
                .build()
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static String serialize(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(String obj, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(obj, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().equals("");
    }

    public static boolean isEmpty(Integer value) {
        return value == null || value == 0;
    }

    public static boolean isEmpty(Long value) {
        return value == null || value == 0;
    }

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(Collection<?> value) {
        return value == null || value.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> value) {
        return value == null || value.isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(Long value) {
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(Integer value) {
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(Collection<?> value) {
        return !isEmpty(value);
    }

    public static boolean isNotEmpty(Object value) {
        return !isEmpty(value);
    }
}
