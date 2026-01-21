package com.imran.shop.shop_management.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {
    @Value("${brevo.api.key}")
    private String apiKey;

    private static final String URL =
            "https://api.brevo.com/v3/smtp/email";

    public void sendEmail(String to, String subject, String html) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        System.out.println(apiKey);
        headers.set("api-key", apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of(
                "email", "imran09cs@gmail.com",
                "name", "Shop Management"
        ));
        body.put("to", List.of(Map.of("email", to)));
        body.put("subject", subject);
        body.put("htmlContent", html);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(URL, request, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Brevo API email failed: " + e.getMessage()
            );
        }
    }

}


