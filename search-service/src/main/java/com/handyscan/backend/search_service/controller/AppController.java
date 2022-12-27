package com.handyscan.backend.search_service.controller;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/v1")
public class AppController {
    public UUID uuid = UUID.randomUUID();

    @GetMapping("/sup")
    @Operation(summary = "Api responds with sup and a unique identifier")
    public String sup() {
        return "Sup search " + uuid.toString();
    }
}
