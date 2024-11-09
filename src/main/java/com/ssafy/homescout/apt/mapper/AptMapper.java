package com.ssafy.homescout.apt.mapper;

import com.ssafy.homescout.apt.dto.AptPosResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AptMapper {

    List<AptPosResponseDto> selectAllAptPos();

}
