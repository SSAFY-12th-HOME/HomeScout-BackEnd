package com.ssafy.homescout.podcast.mapper;

import com.ssafy.homescout.entity.Dongcode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PodcastMapper {

    List<Dongcode> selectAllDongCd();
}
