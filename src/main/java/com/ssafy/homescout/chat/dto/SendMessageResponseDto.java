package com.ssafy.homescout.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageResponseDto { //메시지 전송 후 서버에서 클라이언에게 응답으로 보내는 Dto

    private Long messageId;         // 메시지 ID
    private Long chatRoomId;        // 채팅 방 ID
    private Long userId;            // 메시지 보낸 사용자 ID
    private String content;         // 메시지 내용
    private String messageType;     // 메시지 타입
    private LocalDateTime createdAt; // 메시지 생성 시간
    private String fileUrl;         // 파일 URL (옵션)

}
