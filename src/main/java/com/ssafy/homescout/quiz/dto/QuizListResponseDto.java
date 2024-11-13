package com.ssafy.homescout.quiz.dto;

import com.ssafy.homescout.entity.QuizUser;
import com.ssafy.homescout.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuizListResponseDto {

    private Long quizId;
    private String title;
    private String desc;
    private String tag1;
    private String tag2;
    private String tag3;
    private Boolean isSolve;
    private WriterDto writer;

    public static QuizListResponseDto of(QuizUser quizUser, Boolean isSolve) {
        return QuizListResponseDto.builder()
                .quizId(quizUser.getQuizId())
                .title(quizUser.getTitle())
                .desc(quizUser.getDesc())
                .tag1(quizUser.getExp() + "경험치")
                .tag2(quizUser.getSolvedCount() + "명 참여")
                .tag3(quizUser.getType())
                .isSolve(isSolve)
                .writer(WriterDto.of(quizUser.getUser()))
                .build();
    }

    @Getter
    @Setter
    @Builder
    public static class WriterDto {

        private Long userId;
        private String nickname;
        private String profileImg;

        public static WriterDto of(User user) {
            return WriterDto.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .profileImg(user.getProfileImg())
                    .build();
        }
    }

}
