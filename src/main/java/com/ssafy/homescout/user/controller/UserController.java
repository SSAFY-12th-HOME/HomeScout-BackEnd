package com.ssafy.homescout.user.controller;

import com.ssafy.homescout.user.dto.LoginRequestDto;
import com.ssafy.homescout.user.dto.LoginResponseDto;
import com.ssafy.homescout.user.dto.UserRegisterDto;
import com.ssafy.homescout.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> signUp(@Valid @RequestBody UserRegisterDto registerDto) {
        userService.signUp(registerDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {

        userService.login(loginRequestDto, session);
        return ResponseEntity.ok("로그인에 성공했습니다.");

    }
}
