package com.example.kafkastreamservice.processor;

import com.example.kafkastreamservice.dto.JobData;
import com.example.kafkastreamservice.taskmanager.TaskManager;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.Record;

public class TaskSchedulingProcessor implements Processor<String, JobData, String, JobData> {
    private TaskManager taskManager;

    @Override
    public void init(org.apache.kafka.streams.processor.api.ProcessorContext<String, JobData> context) {
        Processor.super.init(context);
        this.taskManager = new TaskManager(context);
    }

    @Override
    public void process(Record<String, JobData> record) {
        taskManager.handleTask(record);
    }
}
