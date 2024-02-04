package com.example.tempservice.controller;

import com.example.tempservice.dto.request.MockRequest;
import com.example.tempservice.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ServiceController {
    final private CommandService commandService;

    @PostMapping("/send")
    public void sendMockData(@RequestBody MockRequest request) {
        commandService.sendMockData(request);
    }
}
