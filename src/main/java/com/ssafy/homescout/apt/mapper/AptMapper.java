package com.ssafy.homescout.apt.mapper;

import com.ssafy.homescout.apt.dto.AptLifeStory;
import com.ssafy.homescout.apt.dto.AptPosResponseDto;
import com.ssafy.homescout.apt.dto.AptSaleInfo;
import com.ssafy.homescout.entity.Apt;
import com.ssafy.homescout.entity.AptDeal;
import com.ssafy.homescout.entity.Dongcode;
import com.ssafy.homescout.entity.Subway;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AptMapper {

    List<AptPosResponseDto> selectAllAptPos();

    Apt selectAptByAptId(String aptId);

    List<AptSaleInfo> selectAptSalesByAptId(String aptId);

    List<AptDeal> selectAptDealsByAptId(String aptId);

    List<Subway> selectSubwaysByLatLng(@Param("lat") String lat, @Param("lng") String lng);

    List<AptLifeStory> selectAptLifeStoriesByAptId(String aptId);

    Dongcode selectDongcodeByDongCd(String dongCd);

    void insertLifeStory(@Param("aptId") String aptId, @Param("userId") Long userId, @Param("content") String content);
}
