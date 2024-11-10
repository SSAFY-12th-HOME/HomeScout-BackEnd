package com.ssafy.homescout.apt.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LifeStoryResponseDto {

    private Long lifeStoryId;
    private String aptId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

}
