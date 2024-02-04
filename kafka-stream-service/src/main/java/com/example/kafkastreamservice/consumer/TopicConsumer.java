package com.example.kafkastreamservice.consumer;

import com.example.kafkastreamservice.dto.JobData;
import com.example.kafkastreamservice.supplier.StoreProcessorSupplier;
import com.example.kafkastreamservice.supplier.TaskSchedulingProcessorSupplier;
import com.example.kafkastreamservice.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component("processTask")
@RequiredArgsConstructor
public class TopicConsumer implements Consumer<KStream<String, JobData>> {
    private final Serde<JobData> jobDataSerde;
    private final TaskSchedulingProcessorSupplier taskSchedulingProcessorSupplier;
    private final StoreProcessorSupplier storeProcessorSupplier;

    @Override
    public void accept(KStream<String, JobData> input) {
        input
                .peek((key, value) -> {
                    System.out.println("key: " + key + "\nvalue: " + Util.serialize(value));
                })
                .process(storeProcessorSupplier, "jobData", "scheduledData")
                .peek((key, value) -> log.info("Saved Data key: {} with value: {} in State Store", key, Util.serialize(value)))
                .process(taskSchedulingProcessorSupplier, "jobData", "scheduledData")
                .peek((key, value) -> log.info("Processed Data key: {} with value: {}", key, Util.serialize(value)))
                .split()
                .branch((key, value) -> value.getJobType().equals("pendingToExpired"),
                        Branched.withConsumer(ks -> ks.to((key, value, recordContext) -> getTopicNameByType(value.getJobType()), Produced.with(Serdes.String(), jobDataSerde))))
                .branch((key, value) -> value.getJobType().equals("pendingToPaid"),
                        Branched.withConsumer(ks -> ks.to((key, value, recordContext) -> getTopicNameByType(value.getJobType()), Produced.with(Serdes.String(), jobDataSerde))))
                .noDefaultBranch();
    }

    private String getTopicNameByType(String type) {
        if (type.equals("pendingToExpired")) {
            return "process-pending-topic";
        } else {
            return "process-pending-to-paid-topic";
        }
    }
}
