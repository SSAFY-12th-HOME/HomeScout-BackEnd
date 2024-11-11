package com.ssafy.homescout.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class WebClientService {

    @Value("${kakao.api.key}")
    private String KAKAO_API_KEY;

    public Map<String, Object> getPosByAddress(String address) {
        WebClient webClient =
                WebClient
                        .builder()
                        .baseUrl("https://dapi.kakao.com")
                        .build();

        Map<String, Object> response =
                webClient
                        .get()
                        .uri(uriBuilder ->
                                uriBuilder
                                        .path("/v2/local/search/address")
                                        .queryParam("analyze_type", "exact")
                                        .queryParam("page", "1")
                                        .queryParam("size", "1")
                                        .queryParam("query", address)
                                        .build())
                        .header("Authorization", "KakaoAK " + KAKAO_API_KEY)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block();

        return response;
    }

}
