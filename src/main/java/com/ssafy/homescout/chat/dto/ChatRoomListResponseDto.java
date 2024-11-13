package com.ssafy.homescout.chat.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomListResponseDto {
    private Long userId;
    private List<ChatRoomDetailResponseDto> chatRooms;
}