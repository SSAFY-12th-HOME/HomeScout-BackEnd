package com.ssafy.homescout.chat.service;

import com.ssafy.homescout.chat.dto.AddUserRequestDto;
import com.ssafy.homescout.chat.dto.AddUserResponseDto;
import com.ssafy.homescout.chat.dto.SendMessageRequestDto;
import com.ssafy.homescout.chat.dto.SendMessageResponseDto;
import com.ssafy.homescout.chat.mapper.ChatRoomMapper;
import com.ssafy.homescout.chat.mapper.MessageMapper;
import com.ssafy.homescout.entity.ChatRoom;
import com.ssafy.homescout.entity.Message;
import com.ssafy.homescout.entity.User;
import com.ssafy.homescout.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomMapper chatRoomMapper;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    //두 사용자 간의 채팅을 조회한다. 만약 없으면 채팅방을 생성한다.
    public ChatRoom getOrCreateChatRoom(Long user1Id, Long user2Id) {

        // 기존 채팅 방 조회
        ChatRoom chatRoom = chatRoomMapper.getChatRoom(user1Id, user2Id);

        //만약 채팅방이 존재하지 않으면 새로운 채팅방 생성
        if (chatRoom == null) {
            chatRoom = new ChatRoom();
            chatRoom.setUser1Id(user1Id);
            chatRoom.setUser2Id(user2Id);
            chatRoomMapper.createChatRoom(chatRoom);
        }
        return chatRoom;
    }

    //메시지를 저장하고 메시지 정보를 반환한다.
    public SendMessageResponseDto sendMessage(SendMessageRequestDto sendMessageRequestDto) {
        // 채팅 방 조회 또는 생성
        ChatRoom chatRoom = getOrCreateChatRoom(sendMessageRequestDto.getSenderId(), sendMessageRequestDto.getReceiverId());

        // 메시지 엔티티 생성
        Message message = new Message();
        message.setChatRoomId(chatRoom.getChatRoomId());
        message.setUserId(sendMessageRequestDto.getSenderId());
        message.setContent(sendMessageRequestDto.getContent());
        message.setMessageType("CHAT"); // 기본 메시지 타입 설정 : 현재 일반 채팅 메시지 상태, 추후 확장성을 위해 추가(사용자 참여, 퇴장 , 시스템 알림 메시지 등)
        message.setCreatedAt(LocalDateTime.now());
        message.setFileUrl(sendMessageRequestDto.getFileUrl());

        // 메시지 저장
        messageMapper.insertMessage(message);

        // 응답 DTO 생성
        SendMessageResponseDto responseDto = new SendMessageResponseDto();
        responseDto.setMessageId(message.getMessageId());
        responseDto.setChatRoomId(message.getChatRoomId());
        responseDto.setUserId(message.getUserId());
        responseDto.setContent(message.getContent());
        responseDto.setMessageType(message.getMessageType());
        responseDto.setCreatedAt(message.getCreatedAt());
        responseDto.setFileUrl(message.getFileUrl());

        return responseDto;
    }

    //사용자 참여를 처리하고, 응답 DTO를 반환.
    //사용자가 채팅 세션에 참여하려는 시도가 성공했는지 실패했는지를 클라이언트에게 전달하는 역할 -> 사용자 참여 상태에 따라 적절한 업데이트 or 오류처리 수행
    public AddUserResponseDto addUser(AddUserRequestDto addUserRequestDto) {
        // 사용자 존재 여부 확인

        User user = userMapper.findUserByUserId(addUserRequestDto.getUserId());
        AddUserResponseDto responseDto = new AddUserResponseDto();


        // 사용자 참여 성공
        if (user != null) {
            responseDto.setUserId(user.getUserId());
            responseDto.setStatus("SUCCESS");
        }

        // 사용자 존재하지 않음
        else {
            responseDto.setUserId(null);
            responseDto.setStatus("FAIL");
        }

        return responseDto;
    }

    //해당 특정방의 모든 메시지를 조회
    public List<Message> getMessagesByChatRoomId(Long chatRoomId) {
        return messageMapper.getMessagesByChatRoomId(chatRoomId);
    }

}
