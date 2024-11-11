package com.ssafy.homescout.apt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AptResponseDto {

    private String aptId;
    private AptInfo aptInfo;
    private List<AptSaleInfo> sale;
    private List<AptDealInfo> dealHistory;
    private List<AptSubwayInfo> subway;
    private List<AptLifeStory> lifeStory;

    public static AptResponseDto of(String aptId, AptInfo aptInfo, List<AptSaleInfo> sale,
                                    List<AptDealInfo> dealHistory, List<AptSubwayInfo> subway,
                                    List<AptLifeStory> lifeStory) {
        return AptResponseDto.builder()
                .aptId(aptId)
                .aptInfo(aptInfo)
                .sale(sale)
                .dealHistory(dealHistory)
                .subway(subway)
                .lifeStory(lifeStory)
                .build();
    }

}
