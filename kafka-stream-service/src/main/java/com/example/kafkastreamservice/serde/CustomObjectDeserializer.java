package com.example.kafkastreamservice.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomObjectDeserializer<T> extends JsonDeserializer<T> {
    public CustomObjectDeserializer(ObjectMapper objectMapper, Class<T> clazz) {
        super(objectMapper);

        // force set default type to fully class name on our packages
        final Map<String, Object> map = new HashMap<>() {{
            put(JsonDeserializer.VALUE_DEFAULT_TYPE, clazz.getName());
        }};
        configure(map, false);

        // Ignore trusted packages since agent will send their package name causing conflict with our backend
        addTrustedPackages("*");
        // Don't use package type set by producer, since it will deserialize to that package
        setUseTypeHeaders(false);
    }
}
