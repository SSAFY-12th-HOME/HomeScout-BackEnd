package com.ssafy.homescout.auth.controller;

import com.ssafy.homescout.auth.dto.EmailCodeRequestDto;
import com.ssafy.homescout.auth.dto.PasswordRequestDto;
import com.ssafy.homescout.auth.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @GetMapping("/email-duplication")
    public ResponseEntity<?> emailDuplication(@Email(message = "올바른 이메일 형식이 아닙니다.")
                                              @NotBlank(message = "이메일은 필수 입력값입니다.")
                                              @RequestParam("email") String email) {
        authService.emailDuplication(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nickname-duplication")
    public ResponseEntity<?> nicknameDuplication(@NotBlank(message = "닉네임은 필수 입력값입니다.")
                                                 @RequestParam("nickname") String nickname) {
        authService.nicknameDuplication(nickname);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/email")
    public ResponseEntity<?> sendAuthCode(@Email(message = "올바른 이메일 형식이 아닙니다.")
                                          @NotBlank(message = "이메일은 필수 입력값입니다.")
                                          @RequestParam("email") String email) {
        authService.sendAuthCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email-code")
    public ResponseEntity<?> checkEmailCode(@Valid
                                            @RequestBody EmailCodeRequestDto emailCodeRequestDto) {
        authService.checkEmailCode(emailCodeRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password")
    public ResponseEntity<?> checkPassword(@Valid
                                           @RequestBody PasswordRequestDto passwordRequestDto) {
        authService.checkPassword(passwordRequestDto);
        return ResponseEntity.ok().build();
    }
}
