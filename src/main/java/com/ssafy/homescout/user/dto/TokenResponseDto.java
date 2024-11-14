package com.ssafy.homescout.user.dto;

import com.ssafy.homescout.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenResponseDto {

    private String token;
    private Long userId;
    private String email;
    private String nickname;
    private String profileImg;
    private String role;

    public static TokenResponseDto of(String token, User user) {
        return TokenResponseDto.builder()
                .token(token)
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .role(user.getRole())
                .build();
    }

}
