package com.ssafy.homescout.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuizUser {

    private Long quizId;
    private String title;
    private String desc;
    private Integer exp;
    private Integer solvedCount;
    private String type;
    private User user;

}
