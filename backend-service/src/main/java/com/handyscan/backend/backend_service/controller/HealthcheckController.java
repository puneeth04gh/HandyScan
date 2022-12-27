package com.handyscan.backend.backend_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {
    @GetMapping("/")
    public String getHealth(){
        return "up";
    }

}
