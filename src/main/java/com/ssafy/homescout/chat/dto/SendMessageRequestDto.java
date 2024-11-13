package com.ssafy.homescout.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequestDto {

    private Long senderId; //발신자 ID
    private Long receiverId; //수신자 ID
    private String content; //메시지 내용
    private String fileUrl; // 파일 URL (옵션)
}


//{"senderId": 1, "receiverId": 2, "content"="하이"}