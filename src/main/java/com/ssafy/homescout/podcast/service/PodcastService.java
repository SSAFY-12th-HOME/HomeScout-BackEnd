
package com.ssafy.homescout.podcast.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ssafy.homescout.map.service.MapService;
import com.ssafy.homescout.podcast.dto.NewsArticle;
import com.ssafy.homescout.podcast.dto.Participant;
import com.ssafy.homescout.podcast.dto.PodcastScript;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PodcastService {

    // 가상 환경의 Python 인터프리터 절대 경로로 설정
    private static final String PYTHON_EXECUTABLE = "/Users/kimjunhyung/ssafyHomePJT/venv/bin/python3";
    // 절대 경로로 지정된 Python 스크립트
    private static final String SCRIPT_PATH = "/Users/kimjunhyung/ssafyHomePJT/scripts/crawler_service.py";
    private static final int MAX_ARTICLE_NUM = 3; //수집할 최대 기사 수

    @Value("${chatgpt.api-key}")
    private String OPENAI_API_KEY;
    private String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";


    private final MapService mapService;

    // JSON 데이터 처리를 위한 ObjectMapper 인스턴스
    private final ObjectMapper objectMapper = new ObjectMapper();


    //주어진 위치(위도, 경도)를 기반으로 부동산 뉴스 데이터를 생성하는 메서드
    //수집된 뉴스 기사 리스트를 반환함
    public PodcastScript generatePodcastData(Double latitude, Double longitude, String userRole) {
        // 위도와 경도를 기반으로 검색 키워드 생성
        String keyword = generateKeyword(latitude, longitude);

        try {
            // 실행할 명령어 배열 구성
            //실제 실행되는 명령어 예시 : python scripts/crawler_service.py "강남구 부동산" 3

            String[] cmd = {
                    "arch",
                    "-arm64",
                    PYTHON_EXECUTABLE,
                    SCRIPT_PATH,
                    keyword,
                    String.valueOf(MAX_ARTICLE_NUM)
            };


            // 명령어 출력 (파이썬 크롤링 디버깅용)
            System.out.println("Executing command:");
            for (String s : cmd) {
                System.out.print(s + " ");
            }
            System.out.println();


            //ProcessBuilder : Java에서 외부 프로그램이나 명령어를 실행할 때 사용하는 클래스
            //실행할 명령어(cmd)를 설정
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            // 에러 스트림을 합치지 않음
            processBuilder.redirectErrorStream(false);

            // 작업 디렉토리를 스크립트가 위치한 디렉토리로 설정
            processBuilder.directory(new File("/Users/kimjunhyung/ssafyHomePJT/scripts/"));


            // ProcessBuilder로 설정된 명령어로 새로운 프로세스를 생성하고 실행하여 해당 프로세스를 제어할 수 있는 Process 객체를 반환
            Process process = processBuilder.start();
            // → 이 시점에서 Python 스크립트가 실행되어 크롤링 시작


            // stdout 읽기
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            // stderr 읽기
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            StringBuilder stdout = new StringBuilder();
            StringBuilder stderr = new StringBuilder();

            String line;

            // stdout 읽기
            while ((line = stdoutReader.readLine()) != null) {
                stdout.append(line).append("\n");
            }

            // stderr 읽기
            while ((line = stderrReader.readLine()) != null) {
                stderr.append(line).append("\n");
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();

            // 프로세스 종료 코드가 0이 아니면 실행 실패를 의미
            // exitCode == 0: 정상 종료
            // exitCode != 0: 비정상 종료 (에러 발생)
            if (exitCode != 0) {
                throw new RuntimeException("Python 스크립트 실행 실패: " + stderr.toString());
            }

            // Python 스크립트가 출력한 JSON 문자열을 Java 객체 리스트로 변환
            // objectMapper.readValue: JSON 문자열을 Java 객체로 변환하는 메서드
            //  new TypeReference<List<NewsArticle>>() {} : JSON 배열을 NewsArticle 객체 리스트로 변환하기 위한 타입 정보
            // JSON 파싱
            // Python 스크립트가 출력한 JSON 문자열을 Java 객체 리스트로 변환
            List<NewsArticle> articles = objectMapper.readValue(
                    stdout.toString(), // stdout의 내용을 문자열로 변환
                    new TypeReference<List<NewsArticle>>() {} // 변환할 타입 정보 제공
            );

            // GPT API를 통해 팟캐스트 대본 생성
            String podcastScriptText = generatePodcastScript(articles, userRole);

            // 참가자 설정
            List<Participant> participants = new ArrayList<>();
            participants.add(new Participant("호스트1", "호스트"));
            participants.add(new Participant("호스트2", "게스트"));

            // PodcastScript 객체 생성
            PodcastScript podcastScript = new PodcastScript();
            podcastScript.setScript(podcastScriptText); //대본 set
            podcastScript.setParticipants(participants); //팟캐스트 참가자 set

            // 변환된 뉴스 기사 리스트 반환
            return podcastScript;

        } catch (Exception e) {
            throw new RuntimeException("크롤링 중 오류 발생: " + e.getMessage(), e);
        }

    }


    //크롤링된 뉴스 데이터를 기반으로 OpenAI의 GPT-4 API를 호출하여 팟캐스트 대본을 생성
    private String generatePodcastScript(List<NewsArticle> newsArticles, String userRole) throws IOException {

        //OkHttpClient : HTTP 요청을 생성하고 전송하며, 응답을 처리하는 기능을 제공
        //기본적으로 OkHttpClient는 10초의 타임아웃을 설정
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        // GPT에 보낼 프롬프트 구성
        String prompt = createPrompt(newsArticles, userRole);

        // 요청 본문 구성
        //ObjectNode: Jackson 라이브러리의 클래스, JSON 객체를 구성
        //createObjectNode(): 새로운 JSON 객체를 생성
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", "gpt-4");
        requestBody.put("temperature", 0.7);

        // 메시지 배열 구성
        ObjectNode userMessage = objectMapper.createObjectNode();
        //GPT에게 프롬프트를 제공하는 사용자 : 일단 user로 설정해놓음
        userMessage.put("role", "user");
        //content: 실제 프롬프트 내용 담는다.
        userMessage.put("content", prompt);

        //messages 배열에 userMessage를 추가하여 전체 요청 본문에 포함시킴
        requestBody.set("messages", objectMapper.createArrayNode().add(userMessage));

        // RequestBody 생성
        //JSON 형식의 요청 본문을 OkHttp가 이해할 수 있는 형태로 변환하여 HTTP 요청에 포함시킵니다.
        RequestBody body = RequestBody.create(
                requestBody.toString(), //JSON 객체를 문자열로 변환
                MediaType.parse("application/json")
        );

        // 요청 생성
        //OpenAI API에 필요한 모든 정보를 포함하는 HTTP POST 요청을 구성
        Request request = new Request.Builder()
                .url(OPENAI_API_URL) // API 엔드포인트 URL
                .post(body) //이전에 생성한 body를 요청 본문으로 설정
                .addHeader("Content-Type", "application/json") //요청 본문의 형식을 명시
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();

        // API 호출
        //OpenAI API에 실제로 HTTP 요청을 보내고, 응답을 기다림
        try (Response response = client.newCall(request).execute()) {

            //응답이 성공적이지 않은 경우
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            //응답 본문을 String 형태로 읽음
            //API에서 반환된 JSON 형식의 응답 데이터를 문자열로 변환하여 저장
            String responseBody = response.body().string();


            // 응답 JSON 파싱 -> 응답 데이터를 JSON 트리로 변환
            ObjectNode responseJson = (ObjectNode) objectMapper.readTree(responseBody);
            String generatedText = responseJson
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content") //"message" 객체에서 "content" 필드에 접근한다. 이는 GPT 모델이 생성한 텍스트 대본임
                    .asText(); //해당 필드의 값을 String으로 변환

            return generatedText; //최종적으로 생성된 팟캐스트 대본 텍스트를 저장하는 변수
        }
    }

    private String createPrompt(List<NewsArticle> newsArticles, String userRole) throws IOException {

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
        - 진행자1: 김부동 (40대 남성, 20년 경력의 부동산 전문가)
        - 진행자2: 이주택 (30대 여성, 부동산 칼럼니스트)
        - 방송 시간: 1분 내외
        - 주요 청취자: %s
        - 팟캐스트 키워드: %s

        [대본 형식 요구사항]
        1. 구조:
           - 인트로 : 인사말과 오늘의 주제 소개
           - 본론 : 뉴스 내용 분석 및 전문가 의견
           - 아웃트로 : 핵심 내용 요약 및 마무리 인사

        2. 대화 형식:
           - [김부동] 대사내용
           - [이주택] 대사내용
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




    private String generateKeyword(Double latitude, Double longitude) {
        String region = mapService.getRegionName(latitude, longitude);
        return region + " 부동산";
    }


}
