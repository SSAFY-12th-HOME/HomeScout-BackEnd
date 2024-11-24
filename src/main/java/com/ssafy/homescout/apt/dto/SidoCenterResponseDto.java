package com.ssafy.homescout.apt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SidoCenterResponseDto {

    private String lat;
    private String lng;
    private String scale;

}
