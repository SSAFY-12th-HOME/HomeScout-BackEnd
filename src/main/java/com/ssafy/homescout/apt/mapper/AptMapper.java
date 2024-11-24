package com.ssafy.homescout.apt.mapper;

import com.ssafy.homescout.apt.dto.*;
import com.ssafy.homescout.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AptMapper {

    List<AptPosResponseDto> selectAllAptPos(String sggCd);

    Apt selectAptByAptId(String aptId);

    List<AptSaleInfo> selectAptSalesByAptId(@Param("aptId") String aptId, @Param("userId") Long userId);

    List<AptDeal> selectAptDealsByAptId(String aptId);

    List<Subway> selectSubwaysByLatLng(@Param("lat") String lat, @Param("lng") String lng);

    List<AptLifeStory> selectAptLifeStoriesByAptId(String aptId);

    Dongcode selectDongcodeByDongCd(String dongCd);

    void insertLifeStory(LifeStory lifeStory);

    LifeStoryResponseDto selectLifeStoryById(Long lifeStoryId);

    List<AptPosResponseDto> selectAptPosByAptNm(String aptNm);

    List<SidoResponseDto> getSido();

    List<GuResponseDto> getGu(String sidoCode);

    List<DongResponseDto> getDong(String guCode);

    void deleteLifeStory(String lifeStoryId);

    SidoCenterResponseDto getSidoCenter(String sidoCode);
}
