package com.ssafy.homescout.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Sale {

    private Long saleId;
    private String aptId;
    private Long userId;
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
