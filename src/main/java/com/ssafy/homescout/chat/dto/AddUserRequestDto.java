package com.ssafy.homescout.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequestDto { //사용자가 채팅에 참여할 때 사용하는 요청 Dto, 채팅방에 사용자를 추가할 때 사용

    private Long userId; //참여하는 사용자 ID
}
