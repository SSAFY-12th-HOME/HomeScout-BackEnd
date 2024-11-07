package com.ssafy.homescout.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User {
    private Long id;

    private String email;

    private String password;

    private String nickname;

    private String profileImg;

    private String phone;

    private String role;

    private Integer exp;

}
