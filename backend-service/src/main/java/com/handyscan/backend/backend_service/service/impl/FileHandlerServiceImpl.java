package com.handyscan.backend.backend_service.service.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.handyscan.backend.backend_service.service.FileHandlerService;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FileHandlerServiceImpl implements FileHandlerService {
    private Integer partsize = 5 * 1024 * 1024;
    @Autowired
    private MinioClient minioClient;

    @Override
    public InputStream readFile(String filename, String path) {
        log.info("Fetching file {} from path {}", filename, path);
        InputStream inputStream = null;
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
            .bucket(path)
            .object(filename)
            .build();
        try{
            inputStream = minioClient.getObject(getObjectArgs);
        }
        catch(Exception e){
            log.error("Exception while reading file {} from path {}", filename, path);
            e.printStackTrace();
        }
        return inputStream;
    }

    @Override
    public boolean saveFile(InputStream inputStream, String fileName, String path) {
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(path)
            .object(fileName).stream(inputStream, -1, partsize).build();
        try{
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(putObjectArgs);
            log.info("Successfully wrote object {} to bucket {}. Tag : {}", fileName, path, objectWriteResponse.etag());
            return true;
        }
        catch(Exception e){
            log.error("Something went wrong while writing the file", e.getMessage());
            e.printStackTrace();
        }
            
        return false;
    }
    
}
