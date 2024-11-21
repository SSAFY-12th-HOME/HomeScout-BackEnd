package com.ssafy.homescout.auth.service;

import com.ssafy.homescout.auth.dto.EmailCodeRequestDto;
import com.ssafy.homescout.auth.dto.PasswordRequestDto;
import com.ssafy.homescout.user.mapper.UserMapper;
import com.ssafy.homescout.util.NumberUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    public static final Map<String, String> emailAuthCode = new HashMap<>();

    // 이메일 중복 체크
    public void emailDuplication(String email) {
        if (userMapper.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }
    }

    // 닉네임 중복 체크
    public void nicknameDuplication(String nickname) {
        if (userMapper.existsByNickname(nickname)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다.");
        }
    }

    // 인증코드 생성 및 이메일 전송
    public void sendAuthCode(String email) {

        // 인증코드 6자리 생성
        String authCode = NumberUtil.generateAuthCode();

        // 메일 전송
        try {
            sendMail(email, authCode);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 전송 중 오류가 발생했습니다.");
        }

        // 인증 코드 Map 저장
        emailAuthCode.put(email, authCode);
        System.out.println(authCode);
    }

    // 이메일 전송
    public void sendMail(String toMail, String authCode) throws MessagingException {
        String fromMail = "brave.knu@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "[HomeScout] 회원가입 인증 코드 안내"; //제목
        String text =
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>\n" +
                        "<div style=\"margin:100px;\">\n" +
                        "    <h1> 안녕하세요.</h1>\n" +
                        "    <h1> 홈스카우트 HomeScout 입니다.</h1>\n" +
                        "    <br>\n" +
                        "        <p> 아래 인증번호를 복사하여 이메일 인증을 완료해 주세요.</p>\n" +
                        "    <br>\n" +
                        "    <div align=\"center\" style=\"border:1px solid black; font-family:verdana;\">\n" +
                        "    <h3> " + authCode + "</h3>\n" +
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

    public void checkEmailCode(EmailCodeRequestDto emailCodeRequestDto) {
        String email = emailCodeRequestDto.getEmail();
        String authCode = emailCodeRequestDto.getEmailCode();

        // 이메일 인증 코드가 Map에 없으면 오류
        if(!emailAuthCode.containsKey(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증코드를 먼저 전송 해주세요.");
        }

        if(!emailAuthCode.get(email).equals(authCode)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다.");
        }
    }

    public void checkPassword(Long userId, PasswordRequestDto passwordRequestDto) {
        String rawPassword = passwordRequestDto.getPassword();
        String encodedPassword = userMapper.findUserByUserId(userId).getPassword();

        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }
    }
}
