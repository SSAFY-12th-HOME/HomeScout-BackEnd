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
    private Boolean isWish;
    private AptInfo aptInfo;
    private List<AptSaleInfo> sale;
    private List<AptDealInfo> dealHistory;
    private List<AptSubwayInfo> subway;
    private List<AptLifeStory> lifeStory;

}
