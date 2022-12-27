package com.handyscan.backend.backend_service.service.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.io.Files;
import com.google.common.net.HttpHeaders;
import com.handyscan.backend.backend_service.model.HandyScanRecord;
import com.handyscan.backend.backend_service.model.ObjectDetails;
import com.handyscan.backend.backend_service.model.ProcessingStatusEnum;
import com.handyscan.backend.backend_service.model.Response;
import com.handyscan.backend.backend_service.model.UserRecord;
import com.handyscan.backend.backend_service.repository.UserRecordRepository;
import com.handyscan.backend.backend_service.service.FileHandlerService;
import com.handyscan.backend.backend_service.service.KafkaService;

import lombok.extern.log4j.Log4j2;

import com.handyscan.backend.backend_service.service.ApplicationService;


@Service
@Log4j2
public class ApplicationServiceImpl implements ApplicationService {

    @Value(value = "${PROCESSING_BUCKET}")
    private String processingBucket;

    @Autowired
    FileHandlerService fileHandlerService;

    @Autowired
    KafkaService kafkaService;

    @Autowired
    UserRecordRepository userRecordRepository;

    @Override
    public Response storeFileInUserUploads(InputStream inputStream, String fileName, String userName, String collection) {
        fileHandlerService.saveFile(inputStream, fileName, processingBucket);
        sendMessageToKafka(fileName, processingBucket, userName, collection);
        updateDatabase(fileName, processingBucket, userName, collection);
        return null;
    }

    private void sendMessageToKafka(String fileName, String bucket, String userName, String collection){
        Map<String,String> message = new HashMap<>();
        message.put("file_name", fileName);
        message.put("bucket", bucket);
        message.put("user_name", userName);
        message.put("collection", collection);
        kafkaService.sendMessageToOcr(message);
    }

    private void updateDatabase(String fileName, String bucket, String userName, String collection){
        HandyScanRecord handyScanRecord = HandyScanRecord.builder()
            .status(ProcessingStatusEnum.OCR)
            .sourceFile(
                ObjectDetails.builder()
                    .bucket(bucket)
                    .fileName(fileName)
                    .build()
                )
            .build();
        Optional<UserRecord> optionalUserRecord = userRecordRepository.findItem(userName, collection);
        UserRecord userRecord;
        if(optionalUserRecord.isPresent()){
            userRecord = optionalUserRecord.get();
            log.info("Found existing collection for the user", userRecord);
            String key = fileName.replaceAll(".", "_");
            userRecord.getFiles().put(Files.getNameWithoutExtension(fileName), handyScanRecord);
        }
        else{
            log.info("Creating new collection for the user");
            userRecord = UserRecord.builder()
            .id(UUID.randomUUID())
            .user(userName)
            .collection(collection)
            .files(Collections.singletonMap(Files.getNameWithoutExtension(fileName), handyScanRecord))
            .build();
        }
        userRecordRepository.save(userRecord);
        log.info("Persisted to db successfully");
    }

    @Override
    public List<String> getCollectionsForUser(String userName) {
        List<UserRecord> userRecords = userRecordRepository.findCollectionsForUser(userName);
        return userRecords.stream().map(UserRecord::getCollection).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, String>> getFilesForCollection(String userName, String collection) {
        Optional<UserRecord> optionalUserRecord = userRecordRepository.findItem(userName, collection);
        List<Map<String, String>> audioFiles = new ArrayList<>();
        if(optionalUserRecord.isPresent()){
            UserRecord userRecord = optionalUserRecord.get();
            audioFiles = userRecord.getFiles().entrySet().stream().map( entry -> {
                Map<String,String> res = new HashMap();
                res.put("fileName", entry.getKey());
                res.put("status", entry.getValue().getStatus().toString());
                return res;
            }).collect(Collectors.toList());
        }
        return audioFiles;
    }

    @Override
    public void downloadAudioFile(String userName, String collection, String fileName, HttpServletResponse response) {
        Optional<UserRecord> optionalUserRecord = userRecordRepository.findItem(userName, collection);
        if(optionalUserRecord.isPresent()){
            UserRecord userRecord = optionalUserRecord.get();
            ObjectDetails audioFileDetails = userRecord.getFiles().get(fileName).getAudioFile();
            log.info("Reading file {} from bucket {}", audioFileDetails.getFileName(), audioFileDetails.getBucket());
            // ObjectDetails audioFileDetails = ObjectDetails.builder().bucket("processing").fileName("Thankyou.mp4").build();
            try(
                InputStream inputStream = fileHandlerService.readFile(audioFileDetails.getFileName(), audioFileDetails.getBucket());
                OutputStream outputStream = response.getOutputStream();
            ){
                response.setStatus(HttpServletResponse.SC_OK);
	            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + audioFileDetails.getFileName() + "\"");
                IOUtils.copy(inputStream, outputStream);
                log.info("Attached file {}",audioFileDetails.getFileName());
                outputStream.flush();
            }
            catch(Exception e){
                log.error("Error while reading the file {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
