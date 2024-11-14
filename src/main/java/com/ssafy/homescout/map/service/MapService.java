package com.ssafy.homescout.map.service;

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

    public List<SafetyScoreResponseDto> getSafetyScore() {
        return mapMapper.selectAllSafetyScore().stream().map(SafetyScoreResponseDto::of).toList();
    }
}
