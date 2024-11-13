package com.ssafy.homescout.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizOption {

    private Long quizOptionId;
    private Long quizQuestionId;
    private String option;
    private Boolean isAnswer;

}
