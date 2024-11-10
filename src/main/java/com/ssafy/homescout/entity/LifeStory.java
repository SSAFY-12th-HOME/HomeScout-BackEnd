package com.ssafy.homescout.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LifeStory {

    private Long lifeStoryId;
    private String aptId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

}
