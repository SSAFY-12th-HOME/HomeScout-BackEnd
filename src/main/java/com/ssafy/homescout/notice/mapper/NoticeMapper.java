package com.ssafy.homescout.notice.mapper;

import com.ssafy.homescout.notice.dto.NoticeDetailResponseDto;
import com.ssafy.homescout.notice.dto.NoticeListResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<NoticeListResponseDto> selectAllNotice();

    NoticeDetailResponseDto selectNoticeById(Long noticeId);
}
