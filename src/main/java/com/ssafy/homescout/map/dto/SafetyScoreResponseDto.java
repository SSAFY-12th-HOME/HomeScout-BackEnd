package com.ssafy.homescout.map.dto;

import com.ssafy.homescout.entity.SafetyScore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SafetyScoreResponseDto {

    private String sggCd;
    private Integer carAccident;
    private Integer fire;
    private Integer crime;
    private Integer lifeSafety;
    private Integer suicide;
    private Integer disease;

    public static SafetyScoreResponseDto of(SafetyScore safetyScore) {
        return SafetyScoreResponseDto.builder()
                .sggCd(safetyScore.getSggCd())
                .carAccident(safetyScore.getCarAccident())
                .fire(safetyScore.getFire())
                .crime(safetyScore.getCrime())
                .lifeSafety(safetyScore.getLifeSafety())
                .suicide(safetyScore.getSuicide())
                .disease(safetyScore.getDisease())
                .build();
    }

}
