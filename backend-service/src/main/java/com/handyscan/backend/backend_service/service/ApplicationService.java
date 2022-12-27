package com.handyscan.backend.backend_service.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.handyscan.backend.backend_service.model.Response;

public interface ApplicationService {
    
    public Response storeFileInUserUploads(InputStream inputStream, String fileName, String username, String collection);

    public List<String> getCollectionsForUser(String userName);

    public List<Map<String, String>> getFilesForCollection(String userName, String collection);

    public void downloadAudioFile(String userName, String collection, String fileName, HttpServletResponse response);
}
