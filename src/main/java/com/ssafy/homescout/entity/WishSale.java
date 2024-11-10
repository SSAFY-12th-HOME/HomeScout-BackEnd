package com.ssafy.homescout.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WishSale {

    private Long wishSaleId;
    private Long saleId;
    private Long userId;

}
