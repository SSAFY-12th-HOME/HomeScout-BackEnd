package com.ssafy.homescout.user.controller;

import com.ssafy.homescout.user.dto.SignupRequestDto;
import com.ssafy.homescout.user.dto.LoginRequestDto;
import com.ssafy.homescout.user.dto.UserInfoResponseDto;
import com.ssafy.homescout.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signUp(signupRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {
        userService.login(loginRequestDto, session);
        return ResponseEntity.ok("로그인에 성공했습니다.");
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(HttpSession session) {
        UserInfoResponseDto userInfo = userService.getUserInfo(session);

        return ResponseEntity.ok(userInfo);
    }

}
