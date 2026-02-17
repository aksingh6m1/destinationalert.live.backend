package com.tuntun.nealert.repository;

import com.tuntun.nealert.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlertRepository extends MongoRepository<Alert, String> {
  //Here string bcoz alert id is string
}
