package com.ssafy.homescout.chat.mapper;

import com.ssafy.homescout.chat.dto.MessageResponseDto;
import com.ssafy.homescout.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    // 특정 채팅방의 모든 메시지 조회
    List<MessageResponseDto> getMessagesByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    //새로운 메시지 추가
    void insertMessage(Message message);
}
