package com.ssafy.homescout.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Message {
    private Long messageId;
    private Long chatRoomId;
    private Long userId;
    private String content;
    private String messageType;
    private LocalDateTime createdAt;
    private String fileUrl;
}
