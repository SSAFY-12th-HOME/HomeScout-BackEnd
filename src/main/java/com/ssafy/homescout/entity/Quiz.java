package com.ssafy.homescout.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Quiz {

    private Long quizId;
    private Long userId;
    private String title;
    private String desc;
    private Integer exp;
    private Integer solvedCount;
    private String type;

}
