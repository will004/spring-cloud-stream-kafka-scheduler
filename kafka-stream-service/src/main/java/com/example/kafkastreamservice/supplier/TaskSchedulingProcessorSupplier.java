package com.example.kafkastreamservice.supplier;

import com.example.kafkastreamservice.dto.JobData;
import com.example.kafkastreamservice.processor.TaskSchedulingProcessor;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;

public class TaskSchedulingProcessorSupplier implements ProcessorSupplier<String, JobData, String, JobData> {

    @Override
    public Processor<String, JobData, String, JobData> get() {
        return new TaskSchedulingProcessor();
    }
}
