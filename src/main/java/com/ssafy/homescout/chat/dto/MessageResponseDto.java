package com.ssafy.homescout.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponseDto {
    private Long messageId;
    private Long chatRoomId;
    private Long userId;
    private String nickname; // 발신자의 닉네임
    private String profileImg; // 발신자의 프로필 사진
    private String content;
    private String messageType;
    //LocalDateTime 객체를 JSON 문자열로 변환할 때 사용자가 정의한 형식으로 변환할 수 있게 해줌
    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime createdAt;
    private String fileUrl; // Optional
}
