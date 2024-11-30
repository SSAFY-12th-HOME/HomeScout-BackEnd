package com.ssafy.homescout.quiz.service;

import com.ssafy.homescout.entity.Quiz;
import com.ssafy.homescout.entity.QuizOption;
import com.ssafy.homescout.entity.QuizQuestion;
import com.ssafy.homescout.entity.User;
import com.ssafy.homescout.quiz.dto.*;
import com.ssafy.homescout.quiz.mapper.QuizMapper;
import com.ssafy.homescout.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {

    private final QuizMapper quizMapper;
    private final UserMapper userMapper;

    public QuizResponseDto getQuiz(Long quizId) {
        // Quiz 불러오기
        Quiz quiz = quizMapper.selectQuizById(quizId);
        if(quiz == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 퀴즈입니다.");
        }

        // QuizQuestion 불러오기
        List<QuizQuestion> quizQuestions = quizMapper.selectQuizQuestionByQuizId(quizId);
        // 질문 DTO 초기화
        List<QuizResponseDto.QuestionDto> questionDtoList = new ArrayList<>();

        // QuizQuestion에 options 설정해주기
        for (QuizQuestion quizQuestion : quizQuestions) {
            List<QuizOption> quizOptions = quizMapper.selectQuizOptionByQuestionId(quizQuestion.getQuizQuestionId());
            List<String> options = quizOptions.stream()
                    .map(QuizOption::getOption)
                    .toList();
            questionDtoList.add(QuizResponseDto.QuestionDto.of(quizQuestion, options));
        }

        // DTO 담아서 반환하기
        return QuizResponseDto.of(quiz, questionDtoList);
    }

    public List<QuizListResponseDto> getAllQuiz(Long userId, String order) {
        return quizMapper.selectAllQuiz(order).stream().map(
                o -> QuizListResponseDto.of(o, quizMapper.existSolveByUserId(o.getQuizId(), userId))
        ).toList();
    }
    
    public void createQuiz(Long userId, QuizCreateRequestDto quizCreateRequestDto) {
        // TODO 사용자 검증 - 부동산유저 && 경험치 1000이상

        // Quiz 엔티티 생성
        Quiz quiz = Quiz.builder()
                .userId(userId)
                .title(quizCreateRequestDto.getTitle())
                .desc(quizCreateRequestDto.getDesc())
                .exp(quizCreateRequestDto.getExp())
                .solvedCount(0)
                .type(quizCreateRequestDto.getType())
                .build();

        // Quiz insert
        quizMapper.insertQuiz(quiz);

        for (QuizCreateRequestDto.QuestionDto questionDto : quizCreateRequestDto.getQuestions()) {
            // QuizQuestion 엔티티 생성
            QuizQuestion quizQuestion = QuizQuestion.builder()
                    .quizId(quiz.getQuizId())
                    .question(questionDto.getQuestion())
                    .build();

            // QuizQuestion insert
            quizMapper.insertQuizQuestion(quizQuestion);

            for (QuizCreateRequestDto.OptionDto optionDto : questionDto.getOptions()) {
                // QuizOption 엔티티 생성
                QuizOption quizOption = QuizOption.builder()
                        .quizQuestionId(quizQuestion.getQuizQuestionId())
                        .option(optionDto.getOption())
                        .isAnswer(optionDto.getIsAnswer())
                        .build();

                // QuizOption insert
                quizMapper.insertQuizOption(quizOption);
            }
        }

        quizMapper.updateExp();
    }

    public void deleteQuiz(Long userId, Long quizId) {
        Quiz quiz = quizMapper.selectQuizById(quizId);
        if(quiz == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 퀴즈입니다.");
        }
        if (!quiz.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "본인이 생성하지 않은 퀴즈입니다.");
        }
        if(quizMapper.existSolve(quizId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "풀이한 회원이 있으므로 삭제가 불가능합니다.");
        }

        quizMapper.deleteQuiz(quizId);
    }

    public QuizSolveResponseDto submitQuizSolve(Long userId, Long quizId, QuizSolveRequestDto quizSolveRequestDto) {
        if(quizMapper.existSolveByUserId(quizId, userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 풀은 문제입니다.");
        }

        Quiz quiz = quizMapper.selectQuizById(quizId);

        // 정답 리스트 받아오기
        List<QuizSolveRequestDto.SolveDto> answerList = quizMapper.selectAnswerByQuizId(quizId);

        // 맞춘 개수 카운트
        int correctCount = 0;
        for (QuizSolveRequestDto.SolveDto solveDto : quizSolveRequestDto.getSolve()) {
            boolean correct = answerList.stream()
                    .filter(o -> o.getQuizQuestionId() == solveDto.getQuizQuestionId()
                            && o.getOption().equals(solveDto.getOption()))
                    .count() == 1L;
            if(correct) correctCount++;
        }

        // 다 맞춘 경우 사용자 경험치 증가 & 풀이한 목록 추가
        if(correctCount == answerList.size()) {
            User user = userMapper.findUserByUserId(userId);
            userMapper.updateUserExp(userId, user.getExp() + quiz.getExp());
            quizMapper.insertUserQuizSolve(userId, quizId);
            quizMapper.updateSolvedCount(quizId, quiz.getSolvedCount() + 1);
        }

        // DTO 담아서 반환
        return QuizSolveResponseDto.of(answerList.size(),
                correctCount,
                answerList.size() == correctCount ? quiz.getExp() : 0);
    }

    public List<QuizListResponseDto> getMyQuiz(Long userId) {
        return quizMapper.getMyQuizList(userId).stream().map(
                o -> QuizListResponseDto.of(o, Boolean.TRUE)
        ).toList();
    }
}
