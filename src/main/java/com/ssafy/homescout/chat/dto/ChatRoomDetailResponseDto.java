package com.ssafy.homescout.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime lastMessageDate;
    private String lastMessageContent;
}
