package com.tuntun.nealert.service;

import com.tuntun.nealert.model.Alert;
import com.tuntun.nealert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    // ✅ Create alert
    public Alert createAlert(Alert alert) {
        return alertRepository.save(alert);
    }

    // ✅ Get alerts for a specific user
    public List<Alert> getAlertsByUser(String userId) {
        return alertRepository.findByUserId(userId);
    }

    // ✅ Delete alert (user-safe)
    public void deleteAlert(String alertId, String userId) {
        Alert alert = alertRepository.findByIdAndUserId(alertId, userId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alertRepository.delete(alert);
    }

    // ✅ Distance calculation
    public double calculateDistance(double lat1, double lon1,
                                    double lat2, double lon2) {

        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // ✅ Check alert
    public String checkAlert(String alertId, double userLat, double userLon) {

        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        double distance = calculateDistance(
                userLat, userLon,
                alert.getLatitude(), alert.getLongitude());

        if (distance <= alert.getAlertDistance() && !alert.isTriggered()) {
            alert.setTriggered(true);
            alert.setActive(false);
            alertRepository.save(alert);

            return "ALERT_TRIGGERED : You are near " + alert.getDestinationName()
                    + " | Distance: " + distance + " KM";
        }

        return "No alert. Current distance: " + distance + " KM";
    }
}
