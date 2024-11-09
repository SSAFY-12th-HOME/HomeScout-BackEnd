package com.ssafy.homescout.apt.controller;

import com.ssafy.homescout.apt.service.AptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apt")
@RequiredArgsConstructor
public class AptController {

    private final AptService aptService;

    // 아파트 마커 불러오기
    @GetMapping
    public ResponseEntity<?> getAptAll() {
        return ResponseEntity.ok(aptService.getAptAll());
    }

}
