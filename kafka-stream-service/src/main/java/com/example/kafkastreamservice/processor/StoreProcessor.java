package com.example.kafkastreamservice.processor;

import com.example.kafkastreamservice.dto.JobData;
import com.example.kafkastreamservice.taskmanager.StoreDataTaskManager;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

public class StoreProcessor implements Processor<String, JobData, String, JobData> {
    private StoreDataTaskManager storeDataTaskManager;

    @Override
    public void init(ProcessorContext<String, JobData> context) {
        Processor.super.init(context);
        this.storeDataTaskManager = new StoreDataTaskManager(context);
    }

    @Override
    public void process(Record<String, JobData> record) {
        this.storeDataTaskManager.storeDataToStateStore(record);
    }
}
