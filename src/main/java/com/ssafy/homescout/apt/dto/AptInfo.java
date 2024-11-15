package com.ssafy.homescout.apt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AptInfo {

    private String aptNm;          // 아파트 이름
    private String address;        // 아파트 주소
    private Integer homeCnt;       // 총 세대수
    private Integer buildYear;     // 건축년도
    private Double far;            // 용적률(Floor Area Ratio) - 대지면적 대비 건축물 연면적의 비율(%)
    private Double bcr;            // 건폐율(Building Coverage Ratio) - 대지면적 대비 건축면적의 비율(%)
    private Integer flrCnt;        // 지상 층 수
    private String latitude;       // 위도
    private String longitude;      // 경도

}
