package com.ssafy.homescout.notice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class NoticeDetailResponseDto {

    private Long noticeId;
    private String title;
    private String content;
    private String img;
    private String createdAt;
}
