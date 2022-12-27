package com.handyscan.backend.backend_service.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.handyscan.backend.backend_service.service.ApplicationService;

@SpringBootTest
@ActiveProfiles("test")
public class ApplicationControllerTest {
   @Autowired
   private ApplicationController applicationController;

   @Autowired
   private ApplicationService applicationService;

   @Test
   public void getCollectionsForUserTest() {
    List<String> mockCollections = Collections.singletonList("Mock collection");
    Mockito.when(applicationService.getCollectionsForUser(Mockito.anyString())).thenReturn(mockCollections);
    List<String> collections = applicationController.getCollectionsForUser(Mockito.anyString());
    assertEquals(mockCollections, collections);
   }

   @Test
   public void getFilesForCollection() {
    List<Map<String, String>> mockFiles = Collections.singletonList(Collections.singletonMap("File name", "File.png"));
    Mockito.when(applicationService.getFilesForCollection(Mockito.anyString(), Mockito.anyString())).thenReturn(mockFiles);
    List<Map<String,String>> files = applicationController.getFilesForCollection(Mockito.anyString(), Mockito.anyString());
    assertEquals(mockFiles, files);
   }
}
