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
import java.util.List;
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

    //주어진 좌표로부터 시군구 정보를 가져온다.
    public String getRegionByCoordinates(double longitude, double latitude) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .build();

        Map<String, Object> response = webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/v2/local/geo/coord2regioncode.json")
                                .queryParam("x", longitude)  // 경도(x)
                                .queryParam("y", latitude)   // 위도(y)
                                .queryParam("input_coord", "WGS84")
                                .build())
                .header("Authorization", "KakaoAK " + KAKAO_API_KEY)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // 응답 유효성 검사
        if (response == null || !response.containsKey("documents")) {
            throw new RuntimeException("카카오 API에서 지역 정보를 가져오지 못했습니다.");
        }

        List<Map<String, Object>> documents = (List<Map<String, Object>>) response.get("documents");

        if (documents.isEmpty()) {
            throw new RuntimeException("제공된 좌표에 대한 지역 정보를 찾을 수 없습니다.");
        }

        // 시군구 정보는 "region_2depth_name" 필드에 위치
        String region = (String) documents.get(0).get("region_2depth_name");
        return region;
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

    //주어진 좌표로부터 법정코드의 앞 5자리를 추출하고, 뒤에 "00000"을 붙여 10자리 문자열을 반환
    public String getLegalCodeByCoordinates(double longitude, double latitude) {
        // WebClient를 사용하여 Kakao API 호출 준비
        WebClient webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com")
                .build();

        // Kakao API에 GET 요청을 보내고 응답을 Map 형태로 받습니다.
        Map<String, Object> response = webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/v2/local/geo/coord2regioncode.json")
                                .queryParam("x", longitude)        // 경도(x)
                                .queryParam("y", latitude)         // 위도(y)
                                .queryParam("input_coord", "WGS84") // 좌표 체계 설정
                                .build())
                .header("Authorization", "KakaoAK " + KAKAO_API_KEY) // 인증 헤더 설정
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        // 응답 유효성 검사
        if (response == null || !response.containsKey("documents")) {
            throw new RuntimeException("카카오 API에서 지역 정보를 가져오지 못했습니다.");
        }

        // 'documents' 필드에서 지역 정보 리스트를 추출
        List<Map<String, Object>> documents = (List<Map<String, Object>>) response.get("documents");

        if (documents.isEmpty()) {
            throw new RuntimeException("제공된 좌표에 대한 지역 정보를 찾을 수 없습니다.");
        }

        // 첫 번째 문서에서 'code' 필드를 추출
        String code = (String) documents.get(0).get("code");

        if (code == null || code.length() < 5) {
            throw new RuntimeException("법정코드를 올바르게 추출하지 못했습니다.");
        }

        // 법정코드의 앞 5자리 추출
        String firstFiveDigits = code.substring(0, 5);

        // 뒤에 "00000"을 붙여 최종 10자리 코드 생성
        String finalCode = firstFiveDigits + "00000";

        return finalCode;
    }

}
