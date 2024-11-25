package com.ssafy.homescout.apt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SearchResponseDto {

    private String aptId;
    private String aptNm;
    private String latitude;
    private String longitude;

}
