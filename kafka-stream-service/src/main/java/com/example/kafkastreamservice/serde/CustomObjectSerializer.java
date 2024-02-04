package com.example.kafkastreamservice.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomObjectSerializer<T> extends JsonSerializer<T> {
    public CustomObjectSerializer(ObjectMapper objectMapper) {
        super(new TypeReference<T>() {
        }, objectMapper);
    }
}
