package com.ssafy.homescout.quiz.controller;

import com.ssafy.homescout.annotation.Auth;
import com.ssafy.homescout.quiz.dto.QuizCreateRequestDto;
import com.ssafy.homescout.quiz.dto.QuizSolveRequestDto;
import com.ssafy.homescout.quiz.service.QuizService;
import com.ssafy.homescout.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // 퀴즈 리스트 조회
    @GetMapping
    public ResponseEntity<?> getAllQuiz(@Auth Long userId,
                                        @RequestParam(value = "order", required = false, defaultValue = "latest")
                                            String order) {
        return ResponseEntity.ok(quizService.getAllQuiz(userId, order));
    }

    // 퀴즈 조회
    @GetMapping("/{quizId}")
    public ResponseEntity<?> getQuiz(@PathVariable("quizId") Long quizId) {
        return ResponseEntity.ok(quizService.getQuiz(quizId));
    }

    // 퀴즈 생성
    @PostMapping
    public ResponseEntity<?> createQuiz(@Auth Long userId,
                                        @RequestBody QuizCreateRequestDto quizCreateRequestDto) {
        quizService.createQuiz(userId, quizCreateRequestDto);
        return ResponseEntity.ok().build();
    }

    // 퀴즈 삭제
    @DeleteMapping("/{quizId}")
    public ResponseEntity<?> deleteQuiz(@Auth Long userId,
                                        @PathVariable("quizId") Long quizId) {
        quizService.deleteQuiz(userId, quizId);
        return ResponseEntity.ok().build();
    }

    // 퀴즈 정답 제출
    @PostMapping("/{quizId}/solve")
    public ResponseEntity<?> submitQuizSolve(@Auth Long userId,
                                             @PathVariable("quizId") Long quizId,
                                             @RequestBody QuizSolveRequestDto quizSolveRequestDto) {
        return ResponseEntity.ok(quizService.submitQuizSolve(userId, quizId, quizSolveRequestDto));
    }

}
