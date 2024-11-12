package com.ssafy.homescout.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoom {

    private Long chatRoomId;
    private Long user1Id;
    private Long user2Id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
