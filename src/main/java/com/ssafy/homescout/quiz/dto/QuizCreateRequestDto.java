package com.ssafy.homescout.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuizCreateRequestDto {
    private String title;
    private String desc;
    private Integer exp;
    private String type;
    private List<QuestionDto> questions;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class QuestionDto {
        private String question;
        private List<OptionDto> options;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OptionDto {
        private String option;
        private Boolean isAnswer;
    }
}
