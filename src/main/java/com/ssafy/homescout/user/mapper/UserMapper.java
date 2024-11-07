package com.ssafy.homescout.user.mapper;

import com.ssafy.homescout.entity.User;
import com.ssafy.homescout.user.dto.LoginRequestDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void save(User user);

    boolean existsByEmail(String email);

    User findUserByEmail(LoginRequestDto loginRequestDto);

    boolean existsByNickname(String nickname);

    User findUserByUserId(Long userId);
}
