package com.ssafy.homescout.map.mapper;

import com.ssafy.homescout.entity.SafetyScore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapMapper {

    List<SafetyScore> selectAllSafetyScore(String sidoCd);

}
