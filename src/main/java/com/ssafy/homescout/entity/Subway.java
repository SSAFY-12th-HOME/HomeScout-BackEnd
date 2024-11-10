package com.ssafy.homescout.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Subway {

    private Long subwayId;
    private String stationNm;
    private String lineNm;
    private String lat;
    private String lng;
    private Double distance;

}
