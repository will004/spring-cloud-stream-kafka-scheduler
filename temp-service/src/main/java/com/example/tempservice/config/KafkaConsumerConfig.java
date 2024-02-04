package com.example.tempservice.config;

import com.example.tempservice.dto.JobData;
import com.example.tempservice.serde.CustomJsonDeserializer;
import com.example.tempservice.utility.Util;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConsumerFactory<String, JobData> consumerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(null), new StringDeserializer(), new CustomJsonDeserializer<>(Util.OBJECT_MAPPER, JobData.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, JobData> kafkaListenerContainerFactory(ConsumerFactory<String, JobData> consumerFactory) {
        return new ConcurrentKafkaListenerContainerFactory<>() {{
            setConsumerFactory(consumerFactory);
        }};
    }
}
