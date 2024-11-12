package com.ssafy.homescout.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserResponseDto {

    private Long userId;         // 참여한 사용자 ID
    private String status;       // 참여 상태 (예시: SUCCESS)
}
