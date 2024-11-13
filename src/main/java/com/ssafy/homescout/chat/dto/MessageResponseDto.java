package com.ssafy.homescout.chat.dto;

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
    private LocalDateTime createdAt;
    private String fileUrl; // Optional
}
