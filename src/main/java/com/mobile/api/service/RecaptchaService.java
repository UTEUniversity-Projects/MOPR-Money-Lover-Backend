package com.mobile.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecaptchaService {
    @Value("${recaptcha.site-key}")
    private String siteKey;

    @Value("${recaptcha.secret-key}")
    private String secretKey;

    @Value("${recaptcha.verify-url}")
    private String verifyUrl;

    @Value("${recaptcha.threshold}")
    private double threshold;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateCaptcha(String recaptchaResponse) {
        // Create request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create request body type x-www-form-urlencoded
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", secretKey);
        requestBody.add("response", recaptchaResponse);

        // Send POST request
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(verifyUrl, requestEntity, Map.class);

        // Check response from Google
        if (response.getBody() == null || !response.getBody().containsKey("success")) {
            return false;
        }

        boolean success = (boolean) response.getBody().get("success");
        double score = response.getBody().get("score") != null ? (double) response.getBody().get("score") : 0.0;

        return success && score >= threshold;
    }
}
