package com.ssafy.homescout.podcast.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.homescout.podcast.dto.NewsArticle;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PodcastScriptGeneratorService {

    @Value("${chatgpt.api-key}")
    private String OPENAI_API_KEY;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String OPENAI_API_TTS_URL = "https://api.openai.com/v1/audio/speech";

    private final ObjectMapper objectMapper;


    public ArrayList<String[]> generatePodcastScript(List<NewsArticle> newsArticles, String userRole) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        String prompt = createPrompt(newsArticles, userRole);

        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "gpt-4o-mini");
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

            //디버깅 용
            System.out.println("responseJson!!!! : "+responseJson);

            JsonNode contentNode = responseJson
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content");

            if (contentNode.isMissingNode() || !contentNode.isTextual()) {
                throw new IllegalArgumentException("'content' 필드가 누락되었거나 텍스트 형식이 아닙니다.");
            }

            String contentStr = contentNode.asText();

            String jsonContent = stripCodeBlock(contentStr);

            // "content" 문자열을 JSON으로 파싱
            JsonNode contentJson = objectMapper.readTree(jsonContent);

            // "script" 필드 접근
            JsonNode scriptNode = contentJson.path("script");

            if (scriptNode.isMissingNode()) {
                throw new IllegalArgumentException("Parsed JSON에 'script' 필드가 존재하지 않습니다.");
            }

            if (!scriptNode.isArray()) {
                throw new IllegalArgumentException("'script' 필드는 배열 형식이어야 합니다.");
            }

            ArrayNode scriptArray = (ArrayNode) scriptNode;

            //최종 스크립트
            ArrayList<String[]> scriptList = new ArrayList<>();


            for (JsonNode jsonNode : scriptArray) {
                String[] element = new String[2];

                String speaker = jsonNode.get("speaker").asText();
                String text = jsonNode.get("text").asText();

                element[0] = speaker; //화자
                element[1] = text; //화자가 말한 대본 내용

                scriptList.add(element);

            }

                //System.out.println("대본 리스트 사이즈"+scriptList.size());


            return scriptList;

        } catch (IOException e) {
            throw new RuntimeException("팟캐스트 대본 생성 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * 코드 블록을 제거하는 메서드.
     * 예: ```json\n{...}\n```
     */
    private String stripCodeBlock(String content) {
        if (content.startsWith("```")) {
            int firstNewline = content.indexOf('\n');
            int lastFence = content.lastIndexOf("```");
            if (firstNewline != -1 && lastFence != -1 && lastFence > firstNewline) {
                return content.substring(firstNewline + 1, lastFence).trim();
            }
        }
        return content;
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
         - 진행자1: Host (20년 경력의 부동산 전문가)
         - 진행자2: Guest (부동산 칼럼니스트)
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
         - 강조하고 싶은 단어는 따옴표 사용: '역세권', '신축'

         [JSON 형식]
         {
                 "script" : [
                                 {
                                         "speaker" : "Host" or "Guest"
                                         "text" : String\s
                                 }
                  ]
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

    //음성 생성 api
    public ArrayList<Byte> generateVoice(ArrayList<String[]> podcastScriptText){

        //api로 byte를 받아오는데 해당 바이트를 저장하는 배열
//        ArrayList<byte[]> mp3ByteArray = new ArrayList<>();

        ArrayList<Byte> mp3ByteArray = new ArrayList<>();

        for (int i = 0; i < podcastScriptText.size(); i++) {

//            if(i == 2){ 테스트 용
//                break;
//            }

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", "tts-1");

            String[] scripts = podcastScriptText.get(i);

            requestBody.put("input", scripts[1]); //대본 내용
            requestBody.put("voice", scripts[0].equals("Host") ? "alloy" : "onyx"); //host, guest에 따라 화자 목소리 변경


            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(OPENAI_API_TTS_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                    .build();


            try(Response response = client.newCall(request).execute()) {

                byte[] bytes = response.body().bytes();

                for (byte aByte : bytes) {
                    mp3ByteArray.add(aByte);
                }

                //원래는 S3 URL ??
                //String fileOutputPath = "test+" + i + ".mp3";

                //FileOutputStream fileOutputStream = new FileOutputStream(fileOutputPath);

                //fileOutputStream.write(response.body().bytes());

                //fileOutputStream.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return mp3ByteArray;
    }
}
