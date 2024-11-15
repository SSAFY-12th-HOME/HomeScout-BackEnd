package com.ssafy.homescout.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Apt {

    private String aptId;          // 아파트 고유 식별자
    private String sggCd;          // 시군구 코드
    private String umdCd;          // 읍면동 코드
    private String umdNm;          // 읍면동 이름
    private String jibun;          // 지번 주소
    private String roadNmSggCd;    // 도로명 시군구 코드
    private String roadNm;         // 도로명
    private String roadNmBonbun;   // 도로명주소 본번
    private String roadNmBubun;    // 도로명주소 부번
    private String aptNm;          // 아파트 이름
    private Integer buildYear;     // 건축년도
    private String latitude;       // 위도
    private String longitude;      // 경도

}
