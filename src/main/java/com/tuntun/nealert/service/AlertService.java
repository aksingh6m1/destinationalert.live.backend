package com.tuntun.nealert.service;

import com.tuntun.nealert.model.Alert;
import com.tuntun.nealert.repository.AlertRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {
  // This line is a field declaration inside a class.
  private final AlertRepository alertRepository;

  /// Called Constructor Injection to use the service in the controller
  // public AlertService(AlertRepository alertRepository) {
  // this.alertRepository = alertRepository;
  // } //done by @RequiredArgsConstructor

  // ✅ Method to create a new alert and save it to the database
  public Alert createAlert(Alert alert) {
    return alertRepository.save(alert);
  }

  // ✅ Method to retrieve all alerts from the database
  public List<Alert> getAllAlerts() {
    return alertRepository.findAll();
  }

  // ✅ Distance Calculation Method
  public double calculateDistance(double lat1, double lon1,
      double lat2, double lon2) {

    final int R = 6371; // Radius of earth in KM

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);

    // Haversine formula to calculate distance between two points on the Earth
    // (a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)) φ = latitude λ = longitude
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    // Compute angular distance c = 2 ⋅ atan2( √a, √(1−a) )
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
  }

  // ✅ checkAlert method to determine if the user is within the alert distance and
  // update the alert status accordingly
  public String checkAlert(String alertId, double userLat, double userLon) {

    Alert alert = alertRepository.findById(alertId)
        .orElseThrow(() -> new RuntimeException("Alert not found"));

    double distance = calculateDistance(
        userLat,
        userLon,
        alert.getLatitude(),
        alert.getLongitude());

    if (distance <= alert.getAlertDistance() && !alert.isTriggered()) {
      alert.setTriggered(true);
      alert.setActive(false);
      alertRepository.save(alert);
      return "ALERT_TRIGGERED : You are near " + alert.getDestinationName()
          + " | Distance: " + distance + " KM";

    } else {
      return "No alert. Current distance: " + distance + " KM";
    }
  }

}
