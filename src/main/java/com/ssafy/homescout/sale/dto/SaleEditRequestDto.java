package com.ssafy.homescout.sale.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SaleEditRequestDto {

    private String type;
    private Integer price;
    private Integer deposit;
    private Integer rentalFee;
    private Integer dong;
    private Integer floor;
    private String area;
    private String tag1;
    private String tag2;
    private String tag3;

}
