package com.ssafy.homescout.clova;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.io.IOException;

/**
 * Clova TTS API와 통신하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class ClovaTtsService {


    //id
    @Value("${clova.client-id}")
    private String CLOVA_CLIENT_ID;

    //secret key
    @Value("${clova.client-secret}")
    private String CLOVA_CLIENT_SECRET;

    //api url
    @Value("${clova.tts-api-url}")
    private String CLOVA_TTS_API_URL;

    private final OkHttpClient httpClient = new OkHttpClient();


     //텍스트를 음성으로 변환
     // text : 변환할 텍스트
    public byte[] convertTextToSpeech(String text) throws IOException {

        // 요청 본문 구성
        RequestBody requestBody = new FormBody.Builder()
                .add("speaker", "mijin") // Clova TTS 스피커 선택
                .add("speed", "0") // 속도 설정 (0은 기본 속도)
                .add("text", text) // 변환할 텍스트
                .build();

        // HTTP 요청 생성
        Request request = new Request.Builder()
                .url(CLOVA_TTS_API_URL)
                .post(requestBody)
                .addHeader("X-NCP-APIGW-API-KEY-ID", CLOVA_CLIENT_ID)
                .addHeader("X-NCP-APIGW-API-KEY", CLOVA_CLIENT_SECRET)
                .build();

        // 요청 실행 및 응답 처리
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Clova TTS API 요청 실패: " + response);
            }

            // 음성 데이터 반환
            return response.body().bytes();
        }
    }
}
