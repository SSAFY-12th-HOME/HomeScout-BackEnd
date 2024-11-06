package com.ssafy.homescout.user.service;

import com.ssafy.homescout.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public int select() {
        return userMapper.select();
    }
}
