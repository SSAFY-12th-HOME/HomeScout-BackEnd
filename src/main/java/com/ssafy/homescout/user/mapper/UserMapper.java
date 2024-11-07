package com.ssafy.homescout.user.mapper;

import com.ssafy.homescout.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void save(User user);

    boolean existsByEmail(String email);
}
