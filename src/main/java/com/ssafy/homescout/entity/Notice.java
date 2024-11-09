package com.ssafy.homescout.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Notice {

    private String noticeId;
    private String title;
    private String content;
    private String img;
    private String createdAt;
}
