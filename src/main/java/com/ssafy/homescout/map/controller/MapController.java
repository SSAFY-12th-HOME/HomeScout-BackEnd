package com.ssafy.homescout.map.controller;

import com.ssafy.homescout.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    // 안전 등급 불러오기
    @GetMapping("/safety")
    public ResponseEntity<?> getSafetyScore() {
        return ResponseEntity.ok(mapService.getSafetyScore());
    }

}
