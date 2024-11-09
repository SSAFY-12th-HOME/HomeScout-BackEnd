package com.ssafy.homescout.notice.service;

import com.ssafy.homescout.notice.dto.NoticeListResponseDto;
import com.ssafy.homescout.notice.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public List<NoticeListResponseDto> getAllNotices() {
        return noticeMapper.selectAllNotice();
    }
}
