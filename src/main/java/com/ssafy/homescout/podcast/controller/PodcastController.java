package com.ssafy.homescout.podcast.controller;

import com.ssafy.homescout.podcast.dto.NewsArticle;
import com.ssafy.homescout.podcast.service.PodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/podcast")
public class PodcastController {

    private final PodcastService podcastService;

    //크롤링된 뉴스 기사 목록 응답
    @GetMapping("/generate")
    public ResponseEntity<List<NewsArticle>> generatePodcast(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {
        List<NewsArticle> articles = podcastService.generatePodcastData(latitude, longitude);
        return ResponseEntity.ok(articles);
    }
}
