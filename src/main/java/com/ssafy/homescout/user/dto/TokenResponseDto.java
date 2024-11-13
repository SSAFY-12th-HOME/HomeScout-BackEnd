package com.ssafy.homescout.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenResponseDto {

    private String token;

    public static TokenResponseDto of(String token) {
        return TokenResponseDto.builder()
                .token(token)
                .build();
    }

}
