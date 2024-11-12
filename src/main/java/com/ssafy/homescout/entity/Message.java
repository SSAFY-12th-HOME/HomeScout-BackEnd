package com.ssafy.homescout.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long messageId;
    private Long chatRoomId;
    private Long userId;
    private String content;
    private String messageType;
    private LocalDateTime createdAt;
    private String fileUrl;
}
