package com.example.tempservice.serde;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class CustomJsonSerializer<T> extends JsonSerializer<T> {
    public CustomJsonSerializer(ObjectMapper objectMapper) {
        super(new TypeReference<T>() {
        }, objectMapper);
    }
}
