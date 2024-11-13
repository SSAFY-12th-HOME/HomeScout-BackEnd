package com.ssafy.homescout.chat.controller;

import com.ssafy.homescout.chat.dto.ChatRoomListResponseDto;
import com.ssafy.homescout.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @GetMapping("/rooms/{userId}")
    public ResponseEntity<ChatRoomListResponseDto> getChatRoomsByUserId(@PathVariable("userId") Long userId) {
        ChatRoomListResponseDto responseDto = chatService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok(responseDto);
    }
}
