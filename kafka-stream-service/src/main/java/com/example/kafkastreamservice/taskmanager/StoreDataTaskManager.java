package com.example.kafkastreamservice.taskmanager;

import com.example.kafkastreamservice.dto.JobData;
import com.example.kafkastreamservice.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueStore;

@Slf4j
public class StoreDataTaskManager {
    final private KeyValueStore<String, JobData> jobDataKeyValueStore;

    public StoreDataTaskManager(final ProcessorContext<String, JobData> processorContext) {
        this.jobDataKeyValueStore = processorContext.getStateStore("jobData");
    }

    public void storeDataToStateStore(Record<String, JobData> jobDataRecord) {
        final String identifier = jobDataRecord.key();
        final JobData jobData = jobDataRecord.value();

        log.info("Process saving Task with key: {} and data: {} to Job Data state store", identifier, Util.serialize(jobData));

        final JobData existingJobData = jobDataKeyValueStore.get(identifier);

        if (Util.isEmpty(existingJobData) || (existingJobData.getCreatedAt().isBefore(jobData.getCreatedAt()))) {
            log.info("Storing data: {} with identifier: {} to {} state store", Util.serialize(jobData), identifier, "jobData");
            jobDataKeyValueStore.put(identifier, jobData);
        }
    }
}
