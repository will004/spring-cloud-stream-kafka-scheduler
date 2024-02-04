package com.example.tempservice.consumer;

import com.example.tempservice.dto.JobData;
import com.example.tempservice.utility.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {
    private final static String PROCESS_PENDING_TOPIC = "process-pending-topic";
    private final static String PROCESS_PENDING_TO_EXPIRED_TOPIC = "process-pending-to-expired-topic";

    @KafkaListener(topics = PROCESS_PENDING_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void processPendingTopicConsumer(JobData jobData) {
        log.info("Topic: {} receive data: {}", PROCESS_PENDING_TOPIC, Util.serialize(jobData));
    }

    @KafkaListener(topics = PROCESS_PENDING_TO_EXPIRED_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void processPendingToExpiredTopicConsumer(JobData jobData) {
        log.info("Topic: {} receive data: {}", PROCESS_PENDING_TO_EXPIRED_TOPIC, Util.serialize(jobData));
    }
}
