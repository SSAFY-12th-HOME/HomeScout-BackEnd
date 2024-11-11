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

    // 아파트 이름으로 검색
    @GetMapping("/name/{aptNm}")
    public ResponseEntity<?> getAptByAptNm(@PathVariable("aptNm") String aptNm) {
        return ResponseEntity.ok(aptService.findAptByAptNm(aptNm));
    }

    // 시도 데이터 조회
    @GetMapping("/region/sido")
    public ResponseEntity<?> getSido() {
        return ResponseEntity.ok(aptService.getSido());
    }

    // 시군구 데이터 조회
    @GetMapping("/region/gu")
    public ResponseEntity<?> getGu(@RequestParam("sido") String sidoCode) {
        return ResponseEntity.ok(aptService.getGu(sidoCode));
    }

    // 읍면동 데이터 조회
    @GetMapping("/region/dong")
    public ResponseEntity<?> getDong(@RequestParam("gu") String guCode) {
        return ResponseEntity.ok(aptService.getDong(guCode));
    }

}
