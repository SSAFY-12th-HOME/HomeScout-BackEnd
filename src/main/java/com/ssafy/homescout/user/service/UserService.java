package com.ssafy.homescout.user.service;

import com.ssafy.homescout.entity.User;
import com.ssafy.homescout.user.dto.*;
import com.ssafy.homescout.user.mapper.UserMapper;
import com.ssafy.homescout.util.NumberUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender javaMailSender;

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

    public UserInfoResponseDto getUserInfo() {

//        String email = (String) session.getAttribute("loginUser");
        Long userId = 1L; // TODO uesrId 수정

//        User findUser =  userMapper.findUserByEmail("ssafy2@naver.com");
        User user = userMapper.findUserByUserId(userId);

//        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
//                .userId(user.getUserId())
//                .email(user.getEmail())
//                .nickname(user.getNickname())
//                .phone(user.getPhone())
//                .build();

        return UserInfoResponseDto.of(user);
    }

    public MyPageResponseDto getMyPage() {
        Long userId = 1L; // TODO userId 수정
        User user = userMapper.findUserByUserId(userId);
        return MyPageResponseDto.of(user);
    }

    public void editUser(EditUserRequestDto editUserRequestDto) {
        Long userId = 1L; // TODO userId 수정

        // 입력: 비밀번호 O / 비밀번호 확인 X
        if(!editUserRequestDto.getPassword().isEmpty() && editUserRequestDto.getPasswordConfirm().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호 확인을 입력해주세요.");
        }
        // 입력: 비밀번호 X / 비밀번호 확인 O
        if(editUserRequestDto.getPassword().isEmpty() && !editUserRequestDto.getPasswordConfirm().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "변경할 비밀번호를 입력해주세요.");
        }

        // 입력: 비밀번호 O / 비밀번호 확인 O / 두 비밀번호 일치하면 -> 비밀번호 업데이트
        if(!editUserRequestDto.getPassword().isEmpty() &&
                editUserRequestDto.getPassword().equals(editUserRequestDto.getPasswordConfirm())) {
            userMapper.updatePassword(userId, passwordEncoder.encode(editUserRequestDto.getPassword()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "두 비밀번호가 일치하지 않습니다.");
        }

        userMapper.updateProfile(userId, editUserRequestDto.getNickname(), editUserRequestDto.getPhone());
    }

    public void findPassword(FindPasswordRequestDto findPasswordRequestDto) {
        // 이메일과 폰번호로 유저 찾기
        User user = userMapper.findUserByEmailAndPhone(findPasswordRequestDto.getEmail(), findPasswordRequestDto.getPhone());
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일치하는 유저 정보가 없습니다.");
        }

        // 임시 비밀번호 생성
        String tempPassword = NumberUtil.getRandomPassword(8);
        // 메일 전송
        try {
            sendMail(user.getEmail(), tempPassword);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 전송 중 오류가 발생했습니다.");
        }

        // 비밀번호 업데이트
        userMapper.updatePassword(user.getUserId(), passwordEncoder.encode(tempPassword));
    }

    // 이메일 전송
    public void sendMail(String toMail, String tempPassword) throws MessagingException {
        String fromMail = "brave.knu@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "[HomeScout] 임시 비밀번호 발급 안내"; //제목
        String text =
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>\n" +
                        "<div style=\"margin:100px;\">\n" +
                        "    <h1> 안녕하세요.</h1>\n" +
                        "    <h1> 홈스카우트 HomeScout 입니다.</h1>\n" +
                        "    <br>\n" +
                        "        <p> 임시 비밀번호가 발급되었습니다. 빠른 시일 내에 비밀번호를 변경해주세요. </p>\n" +
                        "    <br>\n" +
                        "    <div align=\"center\" style=\"border:1px solid black; font-family:verdana;\">\n" +
                        "    <h3> " + tempPassword + "</h3>\n" +
                        "    </div>\n" +
                        "    <br/>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>";

        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(fromMail); //보내는 이메일
        message.addRecipients(MimeMessage.RecipientType.TO, toMail); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setText(text, "utf-8", "html");

        javaMailSender.send(message);
    }
}
