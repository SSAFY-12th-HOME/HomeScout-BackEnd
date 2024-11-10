package com.ssafy.homescout.apt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AptSubwayInfo {

    private String station;
    private String lineNm;
    private String color;
    private Integer dist;
    private Integer walk;
    private String lat;
    private String lng;

}
