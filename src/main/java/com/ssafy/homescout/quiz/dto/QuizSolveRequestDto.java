package com.ssafy.homescout.quiz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizSolveRequestDto {
    private List<SolveDto> solve;

    @Getter
    @Setter
    public static class SolveDto {
        private int quizQuestionId;
        private String option;
    }
}
