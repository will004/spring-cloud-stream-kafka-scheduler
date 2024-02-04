package com.example.tempservice.config;

import com.example.tempservice.serde.CustomJsonSerializer;
import com.example.tempservice.utility.Util;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {


    @Bean("genericKafkaProducerFactory")
    public ProducerFactory<String, Object> genericKafkaProducerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties(null), new StringSerializer(), new CustomJsonSerializer<>(Util.OBJECT_MAPPER));
    }

    @Bean("genericKafkaTemplate")
    public KafkaTemplate<String, Object> genericKafkaTemplate(ProducerFactory<String, Object> genericKafkaProducerFactory) {
        return new KafkaTemplate<>(genericKafkaProducerFactory);
    }
}
