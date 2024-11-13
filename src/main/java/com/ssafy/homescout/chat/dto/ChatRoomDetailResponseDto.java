package com.ssafy.homescout.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDetailResponseDto {
    private Long chatRoomId;
    private Long otherUserId;
    private String otherUserNickname;
    private String otherUserProfileImg;
    private LocalDateTime lastMessageDate;
    private String lastMessageContent;
}
