package com.example.tempservice.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class JobData {
    private String identifier;
    private String jobType;
    private Instant processedAt;
    private Instant createdAt;
}
