package com.ssafy.homescout.user.dto;

import com.ssafy.homescout.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyPageResponseDto {

    private String nickname;
    private String email;
    private String profileImg;
    private Integer exp;
    private Boolean isBadge;

    public static MyPageResponseDto of(User user) {
        return MyPageResponseDto.builder()
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .email(user.getEmail())
                .exp(user.getExp())
                .isBadge(user.getExp() >= 1000)
                .build();
    }

}
