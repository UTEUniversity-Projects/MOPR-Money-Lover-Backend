package com.mobile.api.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class WebClientService {
    private final RedisService cookieStore;

    public WebClientService(RedisService cookieStore) {
        this.cookieStore = cookieStore;
    }

    public WebClient.RequestHeadersSpec<?> userGetRequest(WebClient webClient, String clientId, String uri) {
        Map<String, String> cookies = cookieStore.getCookies(clientId);
        String cookieHeader = cookies.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "; " + b)
                .orElse("");

        return webClient.get().uri(uri).header(HttpHeaders.COOKIE, cookieHeader);
    }

    public WebClient.RequestBodySpec userPostRequest(WebClient webClient, String clientId, String uri) {
        Map<String, String> cookies = cookieStore.getCookies(clientId);
        String cookieHeader = cookies.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "; " + b)
                .orElse("");

        return webClient.post().uri(uri).header(HttpHeaders.COOKIE, cookieHeader);
    }

    public void storeUserCookies(String clientId, ClientResponse response) {
        Map<String, String> cookies = new HashMap<>();

        response.cookies().forEach((name, list) ->
                list.forEach(cookie -> {
                    cookies.put(name, cookie.getValue());
                    System.out.println(">> Cookie stored: " + name + "=" + cookie.getValue());
                })
        );

        cookieStore.saveCookies(clientId, cookies);
    }

    public void clearUserCookies(String clientId) {
        cookieStore.deleteCookies(clientId);
    }
}
