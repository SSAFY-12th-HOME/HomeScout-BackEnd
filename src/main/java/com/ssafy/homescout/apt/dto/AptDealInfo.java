package com.ssafy.homescout.apt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AptDealInfo {

    private String dealDate;
    private String area;
    private String price;

}
