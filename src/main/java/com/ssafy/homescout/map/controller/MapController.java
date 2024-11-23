package com.ssafy.homescout.map.controller;

import com.ssafy.homescout.map.dto.RegionResponseDto;
import com.ssafy.homescout.map.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    // 안전 등급 불러오기
    @GetMapping("/safety")
    public ResponseEntity<?> getSafetyScore(@RequestParam("sidoCd") String sidoCd) {
        return ResponseEntity.ok(mapService.getSafetyScore(sidoCd));
    }

    @GetMapping("/current-region")
    public ResponseEntity<RegionResponseDto> getCurrentRegion(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {

        String region = mapService.getRegionName(latitude, longitude);
        System.out.println("현재 지역: "+region);
        RegionResponseDto responseDto = new RegionResponseDto(region);
        return ResponseEntity.ok(responseDto);
    }

}
