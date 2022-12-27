package com.handyscan.backend.backend_service.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HandyScanRecord {
    private ProcessingStatusEnum status;
    private ObjectDetails audioFile;
    private ObjectDetails sourceFile;
    private ObjectDetails textFile;
}
