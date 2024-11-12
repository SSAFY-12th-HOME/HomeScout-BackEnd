package com.ssafy.homescout.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {

    private Long chatRoomId;
    private Long user1Id;
    private Long user2Id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
