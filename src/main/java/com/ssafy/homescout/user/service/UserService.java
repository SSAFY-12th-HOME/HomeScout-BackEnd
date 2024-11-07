package com.ssafy.homescout.user.service;

import com.ssafy.homescout.entity.User;
import com.ssafy.homescout.user.dto.UserRegisterDto;
import com.ssafy.homescout.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserRegisterDto registerDto) {
        // 이메일 중복 검사
        if (userMapper.existsByEmail(registerDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }

        // 이메일 인증 코드 확인
//        if (!emailService.verifyEmailCode(registerDto.getEmail(), registerDto.getEmailConfirm())) {
//            throw new IllegalArgumentException("이메일 인증에 실패했습니다.");
//        }

        // 비밀번호 확인
        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        // 사용자 엔티티 생성
        User user = User.builder()
                .email(registerDto.getEmail())
                .password(encodedPassword)
                .nickname(registerDto.getNickname())
                .phone(registerDto.getPhone())
                .build();

        // 사용자 저장
        userMapper.save(user);
    }
}
