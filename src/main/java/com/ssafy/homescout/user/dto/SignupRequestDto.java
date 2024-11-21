package com.ssafy.homescout.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @NotBlank(message = "이메일 인증번호는 필수 입력값입니다.")
    private String emailCode;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z].*)(?=.*[\\d\\W].*).{8,}$|^(?=.*[\\d].*)(?=.*[\\W].*).{8,}$",
            message = "비밀번호는 8자 이상의 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String passwordConfirm;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;

    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;

    @NotBlank(message = "역할은 필수 입력값입니다.")
    private String role;
}