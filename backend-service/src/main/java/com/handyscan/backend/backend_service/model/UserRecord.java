package com.handyscan.backend.backend_service.model;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document("userRecord")
@Data
@Builder
public class UserRecord {
        @Id
        private UUID id;
        private String user;
        private String collection;
        private Map<String, HandyScanRecord> files;
}