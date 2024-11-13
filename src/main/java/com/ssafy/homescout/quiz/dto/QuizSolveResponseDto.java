package com.ssafy.homescout.quiz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizSolveResponseDto {

    private Integer totalCount;
    private Integer correctCount;
    private Integer exp;

    public static QuizSolveResponseDto of(Integer totalCount, Integer correctCount, Integer exp) {
        return QuizSolveResponseDto.builder()
                .totalCount(totalCount)
                .correctCount(correctCount)
                .exp(exp)
                .build();
    }

}
