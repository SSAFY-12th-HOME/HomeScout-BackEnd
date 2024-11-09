package com.ssafy.homescout.apt.service;

import com.ssafy.homescout.apt.dto.AptPosResponseDto;
import com.ssafy.homescout.apt.mapper.AptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AptService {

    private final AptMapper aptMapper;

    public List<AptPosResponseDto> getAptAll() {
        return aptMapper.selectAllAptPos();
    }
}
