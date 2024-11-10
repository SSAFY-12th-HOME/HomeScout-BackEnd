package com.ssafy.homescout.apt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AptInfo {

    private String aptNm;
    private String address;
    private Integer homeCnt;
    private Integer buildYear;
    private Integer maxFloor;
    private Integer far;
    private Integer bcr;
    private Integer dongCnt;
    private String latitude;
    private String longitude;

}
