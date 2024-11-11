package com.ssafy.homescout.user.dto;

import com.ssafy.homescout.entity.User;
import lombok.*;

@Builder
@Getter
@Setter
public class UserInfoResponseDto {

    private Long userId;
    private String email;
    private String nickname;
    private String phone;

    public static UserInfoResponseDto of(User user) {
        return UserInfoResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phone(user.getPhone())
                .build();
    }

}
