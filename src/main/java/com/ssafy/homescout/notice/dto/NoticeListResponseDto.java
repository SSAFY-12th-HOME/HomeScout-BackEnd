package com.ssafy.homescout.notice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NoticeListResponseDto {

    private int noticeId;
    private String title;
    private String createdAt;

}
