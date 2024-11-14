package com.ssafy.homescout.chat.controller;

import com.ssafy.homescout.annotation.Auth;
import com.ssafy.homescout.chat.dto.ChatRoomListResponseDto;
import com.ssafy.homescout.chat.dto.MessageListResponseDto;
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

    //특정 사용자가 참여한 모든 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<ChatRoomListResponseDto> getChatRoomsByUserId(@Auth Long userId) {
        ChatRoomListResponseDto responseDto = chatService.getChatRoomsByUserId(userId);
        return ResponseEntity.ok(responseDto);
    }

    //특정 채팅방의 모든 메시지 조회
    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<MessageListResponseDto> getMessagesByChatRoomId(@PathVariable("chatRoomId") Long chatRoomId) {
        MessageListResponseDto responseDto = chatService.getMessagesByChatRoomId(chatRoomId);
        return ResponseEntity.ok(responseDto);
    }
}
