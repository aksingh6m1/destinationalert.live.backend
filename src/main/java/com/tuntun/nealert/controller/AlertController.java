package com.tuntun.nealert.controller;

import com.tuntun.nealert.model.Alert;
import com.tuntun.nealert.service.AlertService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "https://destinationalert.live",
        "https://www.destinationalert.live"
})
public class AlertController {

    private final AlertService alertService;
    private final RestTemplate restTemplate = new RestTemplate();

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    // ✅ CREATE ALERT
    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.createAlert(alert);
    }

    // ✅ GET ALERTS FOR USER
    @GetMapping
    public List<Alert> getAlertsByUser(@RequestParam String userId) {
        return alertService.getAlertsByUser(userId);
    }

    // ✅ DELETE ALERT
    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable String id,
                            @RequestParam String userId) {
        alertService.deleteAlert(id, userId);
    }

    // ✅ DISTANCE
    @GetMapping("/distance")
    public double getDistance(
            @RequestParam double lat1,
            @RequestParam double lon1,
            @RequestParam double lat2,
            @RequestParam double lon2) {
        return alertService.calculateDistance(lat1, lon1, lat2, lon2);
    }

    // ✅ CHECK ALERT
    @GetMapping("/check")
    public String checkAlert(
            @RequestParam String alertId,
            @RequestParam double userLat,
            @RequestParam double userLon) {
        return alertService.checkAlert(alertId, userLat, userLon);
    }

    // ✅ REVERSE GEOCODING
    @GetMapping("/reverse")
    public ResponseEntity<String> reverseGeocode(
            @RequestParam double lat,
            @RequestParam double lon) {

        try {
            String url = "https://nominatim.openstreetmap.org/reverse?lat="
                    + lat + "&lon=" + lon + "&format=json";

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "DestinationAlertApp");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<Map<String, Object>>() {});

            String address = (String) response.getBody().get("display_name");

            return ResponseEntity.ok(address);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to fetch address");
        }
    }
}
