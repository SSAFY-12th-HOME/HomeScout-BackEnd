package com.ssafy.homescout.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SafetyScore {

    private String sggCd;
    private Integer carAccident;
    private Integer fire;
    private Integer crime;
    private Integer lifeSafety;
    private Integer suicide;
    private Integer disease;

}
