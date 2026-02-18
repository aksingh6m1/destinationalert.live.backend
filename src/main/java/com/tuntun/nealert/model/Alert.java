package com.tuntun.nealert.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "alerts")
public class Alert {

    @Id
    private String id;

    @Indexed
    private String userId;  // ✅ ensures alerts belong to a user

    private String destinationName;
    private double latitude;
    private double longitude;
    private double alertDistance; // in KM

    private boolean active;
    private boolean triggered;
}
