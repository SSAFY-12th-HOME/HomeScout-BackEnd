package com.ssafy.homescout.notice.controller;

import com.ssafy.homescout.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    //공지사항 리스트 불러오기
    @GetMapping
    public ResponseEntity<?> getList(){
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    //게시글 상세 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<?> getById(@PathVariable("noticeId") Long noticeId){
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId));
    }
}
