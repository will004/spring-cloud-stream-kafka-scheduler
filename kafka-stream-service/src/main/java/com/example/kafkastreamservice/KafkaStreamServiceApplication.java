package com.example.kafkastreamservice;

import com.example.kafkastreamservice.dto.JobData;
import com.example.kafkastreamservice.serde.CustomObjectDeserializer;
import com.example.kafkastreamservice.serde.CustomObjectSerializer;
import com.example.kafkastreamservice.util.Util;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KafkaStreamServiceApplication {
    public static void main(String[] args) {
        System.setProperty("spring.cloud.stream.bindings.processTask-in-0.destination", "scheduler-topic");

        SpringApplication.run(KafkaStreamServiceApplication.class, args);
    }
}
