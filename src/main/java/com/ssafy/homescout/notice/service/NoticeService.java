package com.ssafy.homescout.notice.service;

import com.ssafy.homescout.entity.Notice;
import com.ssafy.homescout.notice.dto.NoticeDetailResponseDto;
import com.ssafy.homescout.notice.dto.NoticeEditRequestDto;
import com.ssafy.homescout.notice.dto.NoticeListResponseDto;
import com.ssafy.homescout.notice.dto.NoticeRegistRequestDto;
import com.ssafy.homescout.notice.mapper.NoticeMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public List<NoticeListResponseDto> getAllNotices() {
        return noticeMapper.selectAllNotice();
    }

    public NoticeDetailResponseDto getNoticeById(Long noticeId) {
        NoticeDetailResponseDto notice = noticeMapper.selectNoticeById(noticeId);
        System.out.println(notice);
        //게시글 상세 조회 실패
        if(notice == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다.");
        }

        return noticeMapper.selectNoticeById(noticeId);
    }

    public NoticeDetailResponseDto createNotice(@Valid NoticeRegistRequestDto noticeRegistRequestDto) {
        Notice notice = Notice.builder()
                .title(noticeRegistRequestDto.getTitle())
                .content(noticeRegistRequestDto.getContent())
                .img(noticeRegistRequestDto.getImg())
                .build();

        // Notice 등록
        noticeMapper.insertNotice(notice);

        // 등록된 ID 가져와서 등록된 게시글 상세 조회
        return noticeMapper.selectNoticeById(notice.getNoticeId());
    }

    public NoticeDetailResponseDto editNotice(Long noticeId, NoticeEditRequestDto noticeEditRequestDto) {

        int result = noticeMapper.updateNotice(noticeId, noticeEditRequestDto);

        if(result > 0 ) {
            return noticeMapper.selectNoticeById(noticeId);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"공지사항 수정에 실패했습니다.");
        }
    }

    public void removeNotice(Long noticeId) {

        int result = noticeMapper.deleteNoticeById(noticeId);

        if (result == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"공지사항 삭제에 실패했습니다.");
    }
}

