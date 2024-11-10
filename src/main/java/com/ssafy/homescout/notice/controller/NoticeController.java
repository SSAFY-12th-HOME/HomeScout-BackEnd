package com.ssafy.homescout.notice.controller;

import com.ssafy.homescout.notice.dto.NoticeEditRequestDto;
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

    //공지사항 수정
    @PutMapping("/{noticeId}")
    public ResponseEntity<?> editNotice(@PathVariable("noticeId") Long noticeId,
                                        @RequestBody NoticeEditRequestDto noticeEditRequestDto){
        return ResponseEntity.ok(noticeService.editNotice(noticeId, noticeEditRequestDto));
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<?> deleteNotice(@PathVariable("noticeId") Long noticeId){
        noticeService.removeNotice(noticeId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
