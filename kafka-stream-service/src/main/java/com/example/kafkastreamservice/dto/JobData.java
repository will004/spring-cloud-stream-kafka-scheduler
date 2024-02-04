package com.example.kafkastreamservice.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class JobData {
    private String identifier;
    private String jobType;
    private Instant processedAt;
    private Instant createdAt;
}
