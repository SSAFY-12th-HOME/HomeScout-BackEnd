package com.ssafy.homescout.api.service;
import com.ssafy.homescout.apt.dto.BuildingInfoResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Slf4j
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



    @Value("${api.building-info.service-key}")
    private String serviceKey;


    @Cacheable(value = "buildingInfoCache", key = "#sigunguCd + '-' + #bjdongCd + '-' + #bun + '-' + #ji")
    public BuildingInfoResponseDto getBuildingInfo(String sigunguCd, String bjdongCd, String bun, String ji) {

        log.info("Requesting 파라미터: sigunguCd={}, bjdongCd={}, bun={}, ji={}",
                sigunguCd, bjdongCd, bun, ji);

        try {
            // serviceKey 수동 인코딩
            String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8.toString());

            // 전체 URL 수동 빌드
            String url = String.format("https://apis.data.go.kr/1613000/BldRgstHubService/getBrTitleInfo?serviceKey=%s&sigunguCd=%s&bjdongCd=%s&bun=%s&ji=%s&_type=json",
                    encodedServiceKey, sigunguCd, bjdongCd, bun, ji);

            log.info("Request URL: {}", url);

            // URI 객체 생성
            URI uri = URI.create(url);

            // WebClient 요청
            BuildingInfoResponseDto response = WebClient.builder()
                    .build()
                    .get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(BuildingInfoResponseDto.class)
                    .block();

            return response;

        } catch (Exception e) {
            log.error("Error encoding serviceKey or building the URI", e);
            throw new RuntimeException("Failed to encode serviceKey or build URI", e);
        }
    }
}
