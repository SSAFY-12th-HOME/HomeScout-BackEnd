package com.ssafy.homescout.user.controller;

import com.ssafy.homescout.annotation.Auth;
import com.ssafy.homescout.user.dto.*;
import com.ssafy.homescout.user.service.S3Service;
import com.ssafy.homescout.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signUp(signupRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(userService.login(loginRequestDto));
    }

    @GetMapping
    public ResponseEntity<?> getUserInfo(@Auth Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        userService.addTokenToBlackList(token);
        return ResponseEntity.ok().build();
    }

    // 마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPage(@Auth Long userId) {
        return ResponseEntity.ok(userService.getMyPage(userId));
    }

    // 회원정보 수정
    @PutMapping
    public ResponseEntity<?> editUser(@Auth Long userId,
                                      @Valid @RequestBody EditUserRequestDto editUserRequestDto) {
        userService.editUser(userId, editUserRequestDto);
        return ResponseEntity.ok().build();
    }

    // 비밀번호 찾기 (임시 비밀번호 전송)
    @PutMapping("/password")
    public ResponseEntity<?> findPassword(@Valid @RequestBody FindPasswordRequestDto findPasswordRequestDto) {
        userService.findPassword(findPasswordRequestDto);
        return ResponseEntity.ok().build();
    }

    // 프로필 이미지 업로드
    @PostMapping("/image")
    public ResponseEntity<?> uploadFile(@Auth Long userId,
                             @RequestParam("file") MultipartFile file) {
        String imgUrl = s3Service.uploadImage(file);
        userService.updateProfileImg(userId, imgUrl);
        return ResponseEntity.ok().build();
    }

}
