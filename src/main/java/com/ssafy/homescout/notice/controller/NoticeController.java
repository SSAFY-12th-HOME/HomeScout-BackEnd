package com.ssafy.homescout.notice.controller;

import com.ssafy.homescout.entity.Notice;
import com.ssafy.homescout.notice.dto.NoticeRegistRequestDto;
import com.ssafy.homescout.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //공지사항 상세 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<?> getById(@PathVariable("noticeId") Long noticeId){
        return ResponseEntity.ok(noticeService.getNoticeById(noticeId));
    }

    //공지사항 등록
    @PostMapping
    public ResponseEntity<?> regist(@Valid @RequestBody NoticeRegistRequestDto noticeRegistRequestDto){
        return ResponseEntity.ok(noticeService.createNotice(noticeRegistRequestDto));
    }

}
