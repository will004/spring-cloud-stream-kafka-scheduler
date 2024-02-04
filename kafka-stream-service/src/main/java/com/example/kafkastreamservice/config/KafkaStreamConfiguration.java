package com.example.kafkastreamservice.config;

import com.example.kafkastreamservice.dto.JobData;
import com.example.kafkastreamservice.serde.CustomObjectDeserializer;
import com.example.kafkastreamservice.serde.CustomObjectSerializer;
import com.example.kafkastreamservice.supplier.StoreProcessorSupplier;
import com.example.kafkastreamservice.supplier.TaskSchedulingProcessorSupplier;
import com.example.kafkastreamservice.util.Util;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaStreamConfiguration {
    @Bean
    public Serde<JobData> jobDataSerde() {
        final CustomObjectSerializer<JobData> customObjectSerializer = new CustomObjectSerializer<>(Util.OBJECT_MAPPER);
        final CustomObjectDeserializer<JobData> customObjectDeserializer = new CustomObjectDeserializer<>(Util.OBJECT_MAPPER, JobData.class);

        return Serdes.serdeFrom(customObjectSerializer, customObjectDeserializer);
    }

    @Bean
    public StoreBuilder jobDataStoreBuilder(Serde<JobData> jobDataSerde) {
        return Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("jobData"),
                Serdes.String(),
                jobDataSerde);
    }

    @Bean
    public StoreBuilder scheduledDataStoreBuilder(Serde<JobData> jobDataSerde) {
        return Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("scheduledData"),
                Serdes.String(),
                jobDataSerde);
    }

    @Bean
    public TaskSchedulingProcessorSupplier taskSchedulingProcessorSupplier() {
        return new TaskSchedulingProcessorSupplier();
    }

    @Bean
    public StoreProcessorSupplier storeProcessorSupplier() {
        return new StoreProcessorSupplier();
    }
}
