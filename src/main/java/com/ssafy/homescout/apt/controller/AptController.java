package com.ssafy.homescout.apt.controller;

import com.ssafy.homescout.annotation.Auth;
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
    public ResponseEntity<?> getAptAll(@RequestParam("sgg") String sggCd) {
        return ResponseEntity.ok(aptService.getAptAll(sggCd));
    }

    // 아파트 정보 조회
    @GetMapping("/{aptId}")
    public ResponseEntity<?> getAptInfo(@Auth Long userId,
                                        @PathVariable("aptId") String aptId){
        return ResponseEntity.ok(aptService.getAptInfo(aptId, userId));
    }

    // 살아본 이야기 등록
    @PostMapping("/{aptId}/story")
    public ResponseEntity<?> writeLifeStory(@PathVariable("aptId") String aptId,
                                            @Auth Long userId,
                                            @Valid
                                            @RequestBody LifeStoryRequestDto lifeStoryRequestDto) {
        return ResponseEntity.ok(aptService.writeLifeStory(aptId, userId, lifeStoryRequestDto));
    }

    // 살아본 이야기 삭제
    @DeleteMapping("/{aptId}/story/{lifeStoryId}")
    public ResponseEntity<?> deleteLifeStory(@PathVariable("aptId") String aptId,
                                            @PathVariable("lifeStoryId") String lifeStoryId) {
        aptService.deleteLifeStory(lifeStoryId);
        return ResponseEntity.ok().build();
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

    // 시도 중심 좌표 및 지도 스케일 조회
    @GetMapping("/region/center")
    public ResponseEntity<?> getSidoCenter(@RequestParam("sido") String sidoCode) {
        return ResponseEntity.ok(aptService.getSidoCenter(sidoCode));
    }

    // 읍면동 데이터 조회
    @GetMapping("/region/dong")
    public ResponseEntity<?> getDong(@RequestParam("gu") String guCode) {
        return ResponseEntity.ok(aptService.getDong(guCode));
    }

    // 지역으로 검색
    @GetMapping("/region/{dongCode}")
    public ResponseEntity<?> getPosByDongCode(@PathVariable("dongCode") String dongCode) {
        return ResponseEntity.ok(aptService.getPosByDongCode(dongCode));
    }

}
