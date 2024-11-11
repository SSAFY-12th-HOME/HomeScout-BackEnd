package com.ssafy.homescout.apt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegionPosResponseDto {

    private String latitude;
    private String longitude;

    public static RegionPosResponseDto of(String latitude, String longitude) {
        return RegionPosResponseDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

}
