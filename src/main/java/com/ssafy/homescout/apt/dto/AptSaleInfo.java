package com.ssafy.homescout.apt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AptSaleInfo {

    private Long saleId;
    private String type; // 매매 or 전/월세
    private String price;
    private String deposit;
    private String rentalFee;
    private Integer dong;
    private Integer floor;
    private String area;
    private String tag1;
    private String tag2;
    private String tag3;
    private RealtorInfo realtor;

}
