package com.ssafy.homescout.user.service;

import com.ssafy.homescout.auth.service.AuthService;
import com.ssafy.homescout.entity.User;
import com.ssafy.homescout.user.dto.SignupRequestDto;
import com.ssafy.homescout.user.dto.LoginRequestDto;
import com.ssafy.homescout.user.dto.UserInfoResponseDto;
import com.ssafy.homescout.user.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
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

    public void signUp(SignupRequestDto signupRequestDto) {
        // 이메일 중복 검사
        if (userMapper.existsByEmail(signupRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }

        // 닉네임 중복 검사
        if (userMapper.existsByNickname(signupRequestDto.getNickname())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다.");
        }

        // 비밀번호 확인
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordConfirm())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        // 인증번호 한번 더 확인 (인증번호 확인 API에서 확인했지만, 클라이언트 변조 방지를 위해)
//        if(AuthService.emailAuthCode.getOrDefault(signupRequestDto.getEmail(), "").equals(signupRequestDto.getEmailCode())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다.");
//        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        // 사용자 엔티티 생성
        User user = User.builder()
                .email(signupRequestDto.getEmail())
                .password(encodedPassword)
                .nickname(signupRequestDto.getNickname())
                .phone(signupRequestDto.getPhone())
                .role(signupRequestDto.getRole())
                .build();

        // 사용자 저장
        userMapper.save(user);
    }

    public void login(LoginRequestDto loginRequestDto, HttpSession session) {

        User user = userMapper.findUserByEmail(loginRequestDto.getEmail());

        //로그인 실패
        if(user == null || !passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 잘못되었습니다.");
        }

        //로그인 성공 후 session에 email 저장
        session.setAttribute("loginUser", user.getEmail());

    }

    public UserInfoResponseDto getUserInfo(HttpSession session) {

        String email = (String) session.getAttribute("loginUser");

        User findUser =  userMapper.findUserByEmail("ssafy2@naver.com");

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                .userId(findUser.getUserId())
                .email(findUser.getEmail())
                .nickname(findUser.getNickname())
                .phone(findUser.getPhone())
                .build();

        return userInfoResponseDto;
    }
}
