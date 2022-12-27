package com.handyscan.backend.backend_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.handyscan.backend.backend_service.model.UserRecord;

public interface UserRecordRepository extends MongoRepository<UserRecord, String> {
    
    @Query("{user:'?0', collection:'?1'}")
    Optional<UserRecord> findItem(String username, String collection);

    @Query("{user:'?0'}")
    List<UserRecord> findCollectionsForUser(String username);
}