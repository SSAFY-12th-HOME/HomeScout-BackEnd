package com.ssafy.homescout.map.service;

import com.ssafy.homescout.api.service.WebClientService;
import com.ssafy.homescout.map.dto.SafetyScoreResponseDto;
import com.ssafy.homescout.map.mapper.MapMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final MapMapper mapMapper;
    private final WebClientService webClientService;

    public List<SafetyScoreResponseDto> getSafetyScore(String sidoCd) {
        return mapMapper.selectAllSafetyScore(sidoCd).stream().map(SafetyScoreResponseDto::of).toList();
    }

    public String getRegionName(double latitude, double longitude){
        return webClientService.getRegionByCoordinates(longitude, latitude);
    }

    public String getRegionCode(double latitude, double longitude){
        return webClientService.getLegalCodeByCoordinates(longitude, latitude);
    }
}
