package com.ssafy.homescout.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizQuestion {

    private Long quizQuestionId;
    private Long quizId;
    private String question;

}
