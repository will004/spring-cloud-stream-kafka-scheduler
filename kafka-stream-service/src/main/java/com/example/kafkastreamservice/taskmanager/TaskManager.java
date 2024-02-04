package com.example.kafkastreamservice.taskmanager;

import com.example.kafkastreamservice.dto.JobData;
import com.example.kafkastreamservice.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.Punctuator;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class TaskManager implements Punctuator {
    final private ProcessorContext<String, JobData> processorContext;
    final private KeyValueStore<String, JobData> scheduledDataKeyValueStore;
    final private KeyValueStore<String, JobData> jobDataKeyValueStore;

    public TaskManager(final ProcessorContext<String, JobData> processorContext) {
        this.processorContext = processorContext;
        this.scheduledDataKeyValueStore = processorContext.getStateStore("scheduledData");
        this.jobDataKeyValueStore = processorContext.getStateStore("jobData");

        processorContext.schedule(Duration.ofSeconds(1), PunctuationType.WALL_CLOCK_TIME, this);
    }

    public void handleTask(Record<String, JobData> jobDataRecord) {
        final String identifier = jobDataRecord.key();
        final JobData jobData = jobDataRecord.value();
        log.info("Handling Task with key: {} and data: {}", identifier, Util.serialize(jobData));

        final JobData savedJobData = jobDataKeyValueStore.get(identifier);

        final Instant currentTimestamp = Instant.now();
        if (Util.isNotEmpty(savedJobData) && (savedJobData.getProcessedAt().equals(currentTimestamp) || savedJobData.getProcessedAt().isAfter(currentTimestamp))) {
            log.info("Saving JobData to scheduled state store with key: {} and data: {}", identifier, Util.serialize(jobData));
            scheduledDataKeyValueStore.put(identifier, jobData);
        }
    }

    @Override
    public void punctuate(long timestamp) {
        final Instant currentTimestamp = Instant.now();

        final KeyValueIterator<String, JobData> allJobDataStateStore = jobDataKeyValueStore.all();
        while (allJobDataStateStore.hasNext()) {
            final KeyValue<String, JobData> next = allJobDataStateStore.next();

            final JobData jobData = next.value;
            if (currentTimestamp.equals(jobData.getProcessedAt()) || currentTimestamp.isAfter(jobData.getProcessedAt())) {
                log.info("Inserting jobData to scheduled state store with key: {} and data: {}", next.key, Util.serialize(jobData));
                scheduledDataKeyValueStore.put(next.key, jobData);

                log.info("Deleting Job Data in jobData State Store because processedAt {} is less than now {} with key: {}", jobData.getProcessedAt(), currentTimestamp, next.key);
                jobDataKeyValueStore.delete(next.key);
            }
        }
        allJobDataStateStore.close();

        final KeyValueIterator<String, JobData> allScheduledDataStateStore = scheduledDataKeyValueStore.all();
        while (allScheduledDataStateStore.hasNext()) {
            final KeyValue<String, JobData> next = allScheduledDataStateStore.next();
            log.info("Triggering task Key: {}, Value: {}", next.key, Util.serialize(next.value));

            final JobData jobData = next.value;
            if (jobData.getProcessedAt().isBefore(currentTimestamp)) {
                log.info("Deleting data with identifier: {} in scheduled State Store", next.key);
                scheduledDataKeyValueStore.delete(next.key);
            }

            this.processorContext.forward(new Record<>(next.key, next.value, Instant.now().toEpochMilli()));
        }
        allScheduledDataStateStore.close();
    }
}
