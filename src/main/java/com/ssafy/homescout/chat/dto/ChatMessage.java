package com.ssafy.homescout.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage { //WebSocket을 통해 클라이언트와 서버 간에 실시간으로 주고받는 채팅 메시지의 구조를 정의함.

    private String type;        // 메시지 타입 (예: CHAT, JOIN, LEAVE 등)
    private Long senderId;      // 발신자 ID
    private Long receiverId;    // 수신자 ID
    private String content;     // 메시지 내용
    private String fileUrl;     // 파일 URL (옵션)
}
