package com.ssafy.homescout.notice.mapper;

import com.ssafy.homescout.entity.Notice;
import com.ssafy.homescout.notice.dto.NoticeDetailResponseDto;
import com.ssafy.homescout.notice.dto.NoticeEditRequestDto;
import com.ssafy.homescout.notice.dto.NoticeListResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<NoticeListResponseDto> selectAllNotice();

    NoticeDetailResponseDto selectNoticeById(Long noticeId);

    void insertNotice(Notice notice);

    int updateNotice(@Param("noticeId") Long noticeId, @Param("request") NoticeEditRequestDto noticeEditRequestDto);
}
