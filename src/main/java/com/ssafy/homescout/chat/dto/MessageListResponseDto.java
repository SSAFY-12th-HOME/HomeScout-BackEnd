package com.ssafy.homescout.chat.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageListResponseDto {
    private Long chatRoomId;
    private List<MessageResponseDto> messages;
}
