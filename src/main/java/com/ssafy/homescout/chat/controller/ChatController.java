package com.ssafy.homescout.chat.controller;

import com.ssafy.homescout.chat.dto.*;
import com.ssafy.homescout.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
    WebSocketConfig의 setApplicationDestinationPrefixes("/app") 를 지정했기 때문에
    /app으로 시작하는 모든 메시지가 @MessageMapping 메서드로 라우팅됨
**/

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    //SimpMessagingTemplate : 메시지를 특정 사용자 또는 목적지(destination)로 전송하는 데 사용한다.
    private final SimpMessagingTemplate messagingTemplate;

    //@Payload : JSON → Java 객체 자동 변환 해줌

    @MessageMapping("/chat.sendMessage/{receiverId}")
    public void sendMessage(@Payload SendMessageRequestDto sendMessageRequestDto, @DestinationVariable("receiverId") Long receiverId) {
        // 비즈니스 로직 처리: 메시지를 데이터베이스에 저장하고, 응답 DTO를 반환받음
        SendMessageResponseDto responseDto = chatService.sendMessage(sendMessageRequestDto);



        // ChatMessage DTO : 클라이언트에게 전달할 메시지 데이터 구조
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(responseDto.getMessageType());           // 메시지 타입 (예: CHAT) -> 현재는 모두 CHAT으로 설정됨
        chatMessage.setSenderId(responseDto.getUserId());            // 발신자 ID
        chatMessage.setReceiverId(sendMessageRequestDto.getReceiverId()); // 수신자 ID
        chatMessage.setContent(responseDto.getContent());             // 실제 메시지 내용
        chatMessage.setFileUrl(responseDto.getFileUrl());             // 파일 URL (옵션)

        // 수신자 전용 채널로 메시지 전송
        messagingTemplate.convertAndSend("/queue/messages/" + receiverId.toString(), chatMessage);
        // 발신자 전용 채널로도 메시지 전송 (본인 채팅창 동기화)
        messagingTemplate.convertAndSend("/queue/messages/" + sendMessageRequestDto.getSenderId(), chatMessage);

    }

    //사용자가 채팅에 참여할 때 호출
    //headerAccessor : WebSocket 세션의 헤더 정보를 접근하고 수정할 수 있는 객체
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload AddUserRequestDto addUserRequestDto, SimpMessageHeaderAccessor headerAccessor) {

        // 비즈니스 로직 처리: 사용자의 참여를 처리하고, 응답 DTO를 반환받음(사용자 ID, 참여 상태 (예시: SUCCESS))
        AddUserResponseDto responseDto = chatService.addUser(addUserRequestDto);

        // WebSocket 세션에 사용자 ID 저장: 향후 메시지 전송 시 사용
        //사용자의 ID를 WebSocket 세션의 속성에 저장
        System.out.println("사용자 등록: "+responseDto.getUserId());
        headerAccessor.getSessionAttributes().put("userId", responseDto.getUserId());

        // 사용자에게 참여 상태 전송: 참여 성공 또는 실패 메시지를 보냄
        // convertAndSend : 특정 목적지로 메시지를 전송
        String destination = "/queue/users/" + (responseDto.getUserId() != null ? responseDto.getUserId() : "unknown");
        messagingTemplate.convertAndSend(destination, responseDto);
    }
}
