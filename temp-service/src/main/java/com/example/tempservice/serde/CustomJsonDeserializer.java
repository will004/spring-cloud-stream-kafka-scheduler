package com.example.tempservice.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

public class CustomJsonDeserializer<T> extends JsonDeserializer<T> {
    public CustomJsonDeserializer(ObjectMapper objectMapper, Class<T> clazz) {
        super(objectMapper);
        // force set default type to fully class name on our packages

        final Map<String, Object> map = new HashMap<>() {{
            put(org.springframework.kafka.support.serializer.JsonDeserializer.VALUE_DEFAULT_TYPE, clazz.getName());
        }};
        configure(map, false);

        // Ignore trusted packages since agent will send their package name causing conflict with our backend
        addTrustedPackages("*");
        // Don't use package type set by producer, since it will deserialize to that package
        setUseTypeHeaders(false);
    }
}
