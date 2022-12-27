package com.handyscan.backend.backend_service.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import com.google.gson.Gson;
import com.handyscan.backend.backend_service.service.KafkaService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class KafkaServiceImpl implements KafkaService{

    @Value(value = "${OCR_TOPIC_NAME}")
    private String ocrTopicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessageToOcr(Map<String, String> message) {
        log.info("Publishing message to kafka topic : {}", ocrTopicName);
        Gson gson = new Gson(); 
        String stringifiedMessage = gson.toJson(message);
        ListenableFuture<SendResult<String, String>> result = kafkaTemplate.send(ocrTopicName, stringifiedMessage);
        result.addCallback(this::onSuccess, this::onFailure);
    }
    
    private void onSuccess(SendResult<String, String> sendResult){
        log.info("Sent message successfully {} ", sendResult.toString());
    }

    private void onFailure(Throwable e){
        log.info("Failed to send message. Error {} ", e.getMessage());
    }
}
