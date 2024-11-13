package com.ssafy.homescout.quiz.dto;

import com.ssafy.homescout.entity.Quiz;
import com.ssafy.homescout.entity.QuizQuestion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class QuizResponseDto {

    private Long quizId;
    private String title;
    private int exp;
    private String type;
    private List<QuestionDto> questions;

    @Getter
    @Setter
    @Builder
    public static class QuestionDto {
        private Long quizQuestionId;
        private String question;
        private List<String> options;

        public static QuestionDto of(QuizQuestion quizQuestion, List<String> options) {
            return QuestionDto.builder()
                    .quizQuestionId(quizQuestion.getQuizQuestionId())
                    .question(quizQuestion.getQuestion())
                    .options(options)
                    .build();
        }
    }

    public static QuizResponseDto of(Quiz quiz, List<QuestionDto> questions) {
        return QuizResponseDto.builder()
                .quizId(quiz.getQuizId())
                .title(quiz.getTitle())
                .exp(quiz.getExp())
                .type(quiz.getType())
                .questions(questions)
                .build();
    }

}
