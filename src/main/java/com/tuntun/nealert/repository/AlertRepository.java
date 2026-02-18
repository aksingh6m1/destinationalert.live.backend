package com.tuntun.nealert.repository;

import com.tuntun.nealert.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends MongoRepository<Alert, String> {

    // ✅ Get all alerts for a specific user
    List<Alert> findByUserId(String userId);

    // ✅ Get only active alerts for a user
    List<Alert> findByUserIdAndActiveTrue(String userId);

    // ✅ Secure lookup: alert must belong to the user
    Optional<Alert> findByIdAndUserId(String id, String userId);
}
