package com.ssafy.homescout.user.dto;

import lombok.*;

@Builder
@Getter
@Setter
public class UserInfoResponseDto {

    private Long userId;
    private String email;
    private String nickname;
    private String phone;

}
