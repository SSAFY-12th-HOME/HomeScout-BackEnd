package com.ssafy.homescout.sale.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WishSaleResponseDto {

    private Long wishSaleId;
    private Long saleId;
    private String aptId;
    private String aptNm;
    private String type;
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
