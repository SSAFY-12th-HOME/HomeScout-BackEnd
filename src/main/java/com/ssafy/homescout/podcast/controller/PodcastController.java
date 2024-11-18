package com.ssafy.homescout.podcast.controller;

import com.ssafy.homescout.annotation.Auth;
import com.ssafy.homescout.podcast.dto.NewsArticle;
import com.ssafy.homescout.podcast.dto.PodcastScript;
import com.ssafy.homescout.podcast.service.PodcastService;
import com.ssafy.homescout.user.dto.UserRoleResponseDto;
import com.ssafy.homescout.user.service.UserService;
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
    private final UserService userService; // 사용자 정보를 조회하기 위한 서비스

    //크롤링된 뉴스 기사 목록 응답
    @GetMapping("/generate")
    public ResponseEntity<PodcastScript> generatePodcast(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @Auth Long userId) {

        // 사용자 역할만 조회
        UserRoleResponseDto userRole = userService.getUserRole(userId);

        PodcastScript podcastScript = podcastService.generatePodcastData(latitude, longitude, userRole.getRole());
        return ResponseEntity.ok(podcastScript);
    }



}
