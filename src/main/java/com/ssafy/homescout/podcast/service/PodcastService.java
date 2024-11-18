
package com.ssafy.homescout.podcast.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.homescout.map.service.MapService;
import com.ssafy.homescout.podcast.dto.NewsArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PodcastService {

    // 가상 환경의 Python 인터프리터 절대 경로로 설정
    private static final String PYTHON_EXECUTABLE = "/Users/kimjunhyung/ssafyHomePJT/venv/bin/python3";
    // 절대 경로로 지정된 Python 스크립트
    private static final String SCRIPT_PATH = "/Users/kimjunhyung/ssafyHomePJT/scripts/crawler_service.py";
    private static final int MAX_ARTICLE_NUM = 3; //수집할 최대 기사 수


    private final MapService mapService;

    // JSON 데이터 처리를 위한 ObjectMapper 인스턴스
    private final ObjectMapper objectMapper = new ObjectMapper();


    //주어진 위치(위도, 경도)를 기반으로 부동산 뉴스 데이터를 생성하는 메서드
    //수집된 뉴스 기사 리스트를 반환함
    public List<NewsArticle> generatePodcastData(Double latitude, Double longitude) {

        // 위도와 경도를 기반으로 검색 키워드 생성
        String keyword = generateKeyword(latitude, longitude);
        // 수집할 최대 기사 수 설정

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


            // 명령어 출력 (디버깅용)
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
            // 변환된 뉴스 기사 리스트 반환
            return articles;

        } catch (Exception e) {
            throw new RuntimeException("크롤링 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private String generateKeyword(Double latitude, Double longitude) {
        String region = mapService.getRegionName(latitude, longitude);
        return region + " 부동산";
    }
}
