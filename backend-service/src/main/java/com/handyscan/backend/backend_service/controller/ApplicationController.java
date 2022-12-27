package com.handyscan.backend.backend_service.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.handyscan.backend.backend_service.model.Response;
import com.handyscan.backend.backend_service.service.ApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin
@Log4j2
public class ApplicationController {
    public UUID uuid = UUID.randomUUID();

    @Autowired
    ApplicationService applicationService;

    @PostMapping("/uploadImageAndProcess")
    @Operation(summary = "Api to upload and start the file processing")
    public Response uploadImageAndProcess(
        @RequestParam("file") MultipartFile file, 
        @RequestParam("fileName") String fileName,
        @RequestParam("collection") String collection,
        @RequestParam("userName") String userName) 
            throws Exception {
        log.info("Recieved request for uploading file {} for user {}, collection {}", fileName, userName, collection);
        return applicationService.storeFileInUserUploads(file.getInputStream(), fileName, userName, collection);
    }

    @GetMapping("/getCollectionsForUser")
    @Operation(summary = "Gets all the audio books list that the user has created")
    public List<String> getCollectionsForUser(@RequestParam("userName") String userName) {
        return applicationService.getCollectionsForUser(userName);
    }

    @GetMapping("/getFilesForCollection")
    @Operation(summary = "Gets the list of audio files that have been generated for a collection")
    public List<Map<String, String>> getFilesForCollection(@RequestParam("userName") String userName,@RequestParam("collection") String collection ) {
        return applicationService.getFilesForCollection(userName, collection);
    }

    @GetMapping(path = "/downloadAudioFile")
    public void downloadAudioFile(
        @RequestParam("collection") String collection,
        @RequestParam("userName") String userName,
        @RequestParam("fileName") String fileName,
        HttpServletResponse response
    ) throws Exception {
        log.info("Recieved request to download {} , {} ,{}",userName, collection, fileName);
        applicationService.downloadAudioFile(userName, collection, fileName, response);
    }
}
