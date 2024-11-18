package com.ssafy.homescout.podcast.dto;

import lombok.Data;

@Data
public class NewsArticle { //크롤링된 뉴스 기사를 담을 DTO
    private String title;
    private String content;
    private String keyword;
    private String url;
}
