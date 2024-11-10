package com.ssafy.homescout.sale.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RealtorInfo {

    private Long userId;
    private String nickname;
    private String profileImg;
    private String phone;
    private Boolean isBadge;

}
