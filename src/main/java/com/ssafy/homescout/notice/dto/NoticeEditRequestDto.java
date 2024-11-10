package com.ssafy.homescout.notice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeEditRequestDto {

    private String title;
    private String content;
    private String img;

}
