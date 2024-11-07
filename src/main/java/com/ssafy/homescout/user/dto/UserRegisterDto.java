package com.ssafy.homescout.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    private String emailCode;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    private String passwordConfirm;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;
}