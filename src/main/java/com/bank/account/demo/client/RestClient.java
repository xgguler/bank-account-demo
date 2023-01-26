package com.bank.account.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestClient {

    private final RestTemplate restTemplate;

    @Value("${external-api.status-url}")
    public String STATUS_URL;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Boolean checkStatus() {
        try {
            ResponseEntity<?> response =
                    restTemplate.exchange(STATUS_URL,
                            HttpMethod.GET,
                            null,
                            String.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpStatusCodeException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }
}
