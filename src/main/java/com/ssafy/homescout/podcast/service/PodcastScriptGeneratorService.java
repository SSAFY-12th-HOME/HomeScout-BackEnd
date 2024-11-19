package com.ssafy.homescout.podcast.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.homescout.podcast.dto.NewsArticle;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PodcastScriptGeneratorService {

    @Value("${chatgpt.api-key}")
    private String OPENAI_API_KEY;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    private final ObjectMapper objectMapper;

    public String generatePodcastScript(List<NewsArticle> newsArticles, String userRole) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        String prompt = createPrompt(newsArticles, userRole);

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "gpt-4");
        requestBody.put("temperature", 0.7);

        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        requestBody.set("messages", objectMapper.createArrayNode().add(userMessage));

        RequestBody body = RequestBody.create(
                requestBody.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            ObjectNode responseJson = (ObjectNode) objectMapper.readTree(responseBody);
            String generatedText = responseJson
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            return generatedText;
        } catch (IOException e) {
            throw new RuntimeException("팟캐스트 대본 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private String createPrompt(List<NewsArticle> newsArticles, String userRole){

        //현재 키워드(시군구 + 부동산)
        String keyword = newsArticles.get(0).getKeyword();

        // 청취자 역할 설정
        String listenerRole;
        if ("일반".equals(userRole)) {
            listenerRole = "집을 구하는 소비자";
        } else if ("공인중개사".equals(userRole)) {
            listenerRole = "집을 중개하는 공인중개사";
        } else {
            listenerRole = "청취자";
        }


        // 지시사항 정의
        String instructions = String.format("""
        다음의 부동산 뉴스 기사를 바탕으로 전문적이면서도 친근한 부동산 팟캐스트 대본을 작성해주세요.

        [기본 설정]
        - 진행자1: 김싸피 (20년 경력의 부동산 전문가)
        - 진행자2: 박서울 (부동산 칼럼니스트)
        - 방송 시간: 1분 내외
        - 주요 청취자: %s
        - 팟캐스트 키워드: %s

        [대본 형식 요구사항]
        1. 구조:
           - 인트로 : 인사말과 오늘의 주제 소개
           - 본론 : 뉴스 내용 분석 및 전문가 의견
           - 아웃트로 : 핵심 내용 요약 및 마무리 인사

        2. 대화 형식:
           - [김싸피] 대사내용
           - [박서울] 대사내용
           형식으로 작성

        3. 대화 특징:
           - 전문 용어는 쉽게 풀어서 설명
           - 실제 사례나 통계 데이터 활용
           - 청취자의 관점에서 필요한 정보 강조
           - 자연스러운 대화 흐름 유지

        [TTS 최적화 요구사항]
        - 한 문장은 50자 이내로 작성
        - 문장 끝에 적절한 쉼표(,)와 마침표(.) 사용
        - 감정을 나타내는 표현은 괄호로 표시: (밝게), (진지하게)
        - 강조하고 싶은 단어는 따옴표 사용: '역세권', '신축'

        [JSON 형식]
        {
            "script": {
                "intro": "인트로 대본",
                "main": "메인 대본",
                "outro": "아웃트로 대본"
            }
        }

        다음은 참고할 뉴스 기사입니다:
        """, listenerRole, keyword);

        // 뉴스 데이터 요약
        StringBuilder newsSummary = new StringBuilder();

        for (NewsArticle article : newsArticles) {
            newsSummary.append("제목: ").append(article.getTitle()).append("\n");
            newsSummary.append("내용: ").append(article.getContent()).append("\n\n");
        }

        // 최종 프롬프트
        String prompt = instructions + "\n\n" + newsSummary.toString();

        return prompt;
    }
}
