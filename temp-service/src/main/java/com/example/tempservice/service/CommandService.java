package com.example.tempservice.service;

import com.example.tempservice.dto.JobData;
import com.example.tempservice.dto.request.MockRequest;
import com.example.tempservice.utility.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandService {
    private final KafkaTemplate<String, Object> genericKafkaTemplate;

    public void sendMockData(MockRequest request) {
        log.info("processing mock data...");

        final Instant currentTimestamp = Instant.now();
        final JobData jobData = new JobData()
                .setIdentifier(request.getIdentifier())
                .setJobType(request.getJobType())
                .setProcessedAt(currentTimestamp.plus(30, ChronoUnit.SECONDS))
                .setCreatedAt(currentTimestamp);

        try {
            final String kafkaTopic = "scheduler-topic";
            log.info("processing mock data ===> {}\nsend to topic: {}", Util.serialize(jobData), kafkaTopic);

            final SendResult<String, Object> sendResult = genericKafkaTemplate.send(kafkaTopic, jobData.getIdentifier(), jobData)
                    .get();
            log.info("Sent message {} with metadata {}", Util.serialize(jobData), sendResult.getRecordMetadata().toString());
        } catch (ExecutionException | InterruptedException ex) {
            log.info("Unable to sent message {} because {}", Util.serialize(jobData), ex.getMessage());
        }
    }
}
