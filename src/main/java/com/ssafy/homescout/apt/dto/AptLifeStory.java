package com.ssafy.homescout.apt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AptLifeStory {

    private Long lifeStoryId;
    private String content;
    private LocalDateTime createdAt;
    private String created;
    private WriterInfo writer;

}
