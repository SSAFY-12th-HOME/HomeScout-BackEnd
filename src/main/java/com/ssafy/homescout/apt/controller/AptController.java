package com.ssafy.homescout.apt.controller;

import com.ssafy.homescout.apt.dto.LifeStoryRequestDto;
import com.ssafy.homescout.apt.service.AptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apt")
@RequiredArgsConstructor
@Validated
public class AptController {

    private final AptService aptService;

    // 아파트 마커 불러오기
    @GetMapping
    public ResponseEntity<?> getAptAll() {
        return ResponseEntity.ok(aptService.getAptAll());
    }

    // 아파트 정보 조회
    @GetMapping("/{aptId}")
    public ResponseEntity<?> getAptInfo(@PathVariable("aptId") String aptId) {
        return ResponseEntity.ok(aptService.getAptInfo(aptId));
    }

    // 살아본 이야기 등록
    @PostMapping("/{aptId}/story")
    public ResponseEntity<?> writeLifeStory(@PathVariable("aptId") String aptId,
                                            @Valid
                                            @RequestBody LifeStoryRequestDto lifeStoryRequestDto) {
        return ResponseEntity.ok(aptService.writeLifeStory(aptId, lifeStoryRequestDto));
    }

}
