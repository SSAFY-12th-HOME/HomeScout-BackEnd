package com.ssafy.homescout.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageUrlResponseDto {

    private String imgUrl;

    public static ImageUrlResponseDto of(String imgUrl) {
        return ImageUrlResponseDto.builder()
                .imgUrl(imgUrl)
                .build();
    }

}
