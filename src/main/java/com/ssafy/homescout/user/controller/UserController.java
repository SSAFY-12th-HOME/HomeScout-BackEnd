package com.ssafy.homescout.user.controller;

import com.ssafy.homescout.user.dto.UserRegisterDto;
import com.ssafy.homescout.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
