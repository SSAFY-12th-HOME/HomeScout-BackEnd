package com.ssafy.homescout.chat.mapper;

import com.ssafy.homescout.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

    //해당 채팅방의 메시지 목록을 조회
    List<Message> getMessagesByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    //새로운 메시지 추가
    void insertMessage(Message message);
}
