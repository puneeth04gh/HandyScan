package com.handyscan.backend.backend_service.service;

import java.util.Map;

public interface KafkaService {
    public void sendMessageToOcr(Map<String,String> message);
}
