package com.ssafy.homescout.podcast.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

 // 팟캐스트 대본에서 화자와 대사 내용을 저장하는 DTO 클래스
@Data
@AllArgsConstructor
public class PodcastDialogue {
    private String speaker; // 화자 이름
    private String content; // 대사 내용
}
