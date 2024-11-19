package com.ssafy.homescout.podcast.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.homescout.podcast.dto.NewsArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NewsCrawlerService {

    private static final String PYTHON_EXECUTABLE = "/Users/kimjunhyung/ssafyHomePJT/venv/bin/python3";
    private static final String SCRIPT_PATH = "/Users/kimjunhyung/ssafyHomePJT/scripts/crawler_service.py";
    private static final int MAX_ARTICLE_NUM = 2;

    private final ObjectMapper objectMapper;

    public List<NewsArticle> crawlNewsData(String district) {
        String keyword = district + " 부동산";

        try {
            String[] cmd = {
                    "arch",
                    "-arm64",
                    PYTHON_EXECUTABLE,
                    SCRIPT_PATH,
                    keyword,
                    String.valueOf(MAX_ARTICLE_NUM)
            };

            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            processBuilder.redirectErrorStream(false);
            processBuilder.directory(new File("/Users/kimjunhyung/ssafyHomePJT/scripts/"));

            Process process = processBuilder.start();

            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            StringBuilder stdout = new StringBuilder();
            StringBuilder stderr = new StringBuilder();

            String line;
            while ((line = stdoutReader.readLine()) != null) {
                stdout.append(line).append("\n");
            }

            while ((line = stderrReader.readLine()) != null) {
                stderr.append(line).append("\n");
            }

            boolean exited = process.waitFor(60, TimeUnit.SECONDS);
            if (!exited) {
                process.destroy();
                throw new RuntimeException("Python 스크립트 실행 시간 초과.");
            }

            int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new RuntimeException("Python 스크립트 실행 실패: " + stderr.toString());
            }

            List<NewsArticle> articles = objectMapper.readValue(
                    stdout.toString(),
                    new TypeReference<List<NewsArticle>>() {}
            );

            return articles;

        } catch (Exception e) {
            throw new RuntimeException("크롤링 중 오류 발생: " + e.getMessage(), e);
        }
    }


}
