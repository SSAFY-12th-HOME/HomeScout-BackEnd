package com.ssafy.homescout.chat.mapper;

import com.ssafy.homescout.chat.dto.ChatRoomDetailResponseDto;
import com.ssafy.homescout.entity.ChatRoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChatRoomMapper {

    //두 사용 간의 채팅방 조회
    ChatRoom getChatRoom(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    //새로운 채팅방 생성
    void createChatRoom(ChatRoom chatRoom);

    // 특정 사용자가 참여한 모든 채팅방 조회
    List<ChatRoomDetailResponseDto> getChatRoomDetailsByUserId(@Param("userId") Long userId);

    ChatRoom getChatRoomById(@Param("chatRoomId") Long chatRoomId);
}
