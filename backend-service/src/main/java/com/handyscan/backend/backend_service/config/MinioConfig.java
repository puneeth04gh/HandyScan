package com.handyscan.backend.backend_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
public class MinioConfig {

    @Value("${MINIO_USERNAME}")
    private String minioUserName;

    @Value("${MINIO_PASSWORD}")
    private String mminioPassword;

    @Value("${MINIO_ENDPOINT}")
    private String minioEndpoint;

    @Bean 
    public MinioClient getMinioConfig(){
        log.info("Creating minio client");
        MinioClient minioClient = MinioClient.builder()
              .endpoint(minioEndpoint)
              .credentials(minioUserName, mminioPassword)
              .build();
        log.info("Created minio client");
        return minioClient;
    }
}
