package com.mobile.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecaptchaService {

    @Value("${recaptcha.site-key}")
    private String siteKey;

    @Value("${recaptcha.api-key}")
    private String apiKey;

    @Value("${recaptcha.project-id}")
    private String projectId;

    @Value("${recaptcha.threshold}")
    private double threshold;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean verifyCaptcha(String recaptchaToken) {
        return true;
//        // Prepare the request URL
//        String url = String.format("https://recaptchaenterprise.googleapis.com/v1/projects/%s/assessments?key=%s", projectId, apiKey);
//
//        // Prepare the request body
//        Map<String, Object> event = new HashMap<>();
//        event.put("token", recaptchaToken);
//        event.put("siteKey", siteKey);
//        event.put("expectedAction", "LOGIN");
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("event", event);
//
//        // Set headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        // Send the request
//        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
//
//        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
//            return false;
//        }
//
//        Map<String, Object> responseBody = response.getBody();
//
//        // Extract tokenProperties
//        Map<String, Object> tokenProps = (Map<String, Object>) responseBody.get("tokenProperties");
//        if (tokenProps == null || !(Boolean.TRUE.equals(tokenProps.get("valid")))) {
//            return false;
//        }
//
//        // Extract riskAnalysis
//        Map<String, Object> riskAnalysis = (Map<String, Object>) responseBody.get("riskAnalysis");
//        double score = riskAnalysis != null && riskAnalysis.get("score") instanceof Number
//                ? ((Number) riskAnalysis.get("score")).doubleValue()
//                : 0.0;
//
//        return score >= threshold;
    }
}
