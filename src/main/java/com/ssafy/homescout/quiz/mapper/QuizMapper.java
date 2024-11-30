package com.ssafy.homescout.quiz.mapper;

import com.ssafy.homescout.entity.Quiz;
import com.ssafy.homescout.entity.QuizOption;
import com.ssafy.homescout.entity.QuizQuestion;
import com.ssafy.homescout.entity.QuizUser;
import com.ssafy.homescout.quiz.dto.QuizSolveRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuizMapper {
    void insertQuiz(Quiz quiz);

    void insertQuizQuestion(QuizQuestion quizQuestion);

    void insertQuizOption(QuizOption quizOption);

    Quiz selectQuizById(Long quizId);

    List<QuizQuestion> selectQuizQuestionByQuizId(Long quizId);

    List<QuizOption> selectQuizOptionByQuestionId(Long quizQuestionId);

    List<QuizUser> selectAllQuiz(String order);

    void deleteQuiz(Long quizId);

    Boolean existSolve(Long quizId);

    Boolean existSolveByUserId(@Param("quizId") Long quizId, @Param("userId") Long userId);

    List<QuizSolveRequestDto.SolveDto> selectAnswerByQuizId(Long quizId);

    void insertUserQuizSolve(@Param("userId") Long userId, @Param("quizId") Long quizId);

    void updateSolvedCount(@Param("quizId") Long quizId, @Param("solvedCount") Integer solvedCount);

    List<QuizUser> getMyQuizList(Long userId);

    void updateExp();
}
