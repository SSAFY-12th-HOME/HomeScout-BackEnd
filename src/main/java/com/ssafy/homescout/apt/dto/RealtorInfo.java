package com.ssafy.homescout.apt.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RealtorInfo {

    private Long userId;
    private String nickname;
    private String profileImg;
    private String phone;
    private Boolean isBadge;

}
