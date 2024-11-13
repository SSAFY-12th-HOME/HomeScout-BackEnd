package com.ssafy.homescout.user.mapper;

import com.ssafy.homescout.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void save(User user);

    boolean existsByEmail(String email);

    User findUserByEmail(String email);

    boolean existsByNickname(String nickname);

    User findUserByUserId(Long userId);

    void updatePassword(@Param("userId") Long userId, @Param("encodedPassword") String encodedPassword);

    void updateProfile(@Param("userId") Long userId, @Param("nickname") String nickname, @Param("phone") String phone);

    User findUserByEmailAndPhone(@Param("email") String email, @Param("phone") String phone);

    void updateUserExp(@Param("userId") Long userId, @Param("exp") Integer exp);

    void updateProfileImg(@Param("userId") Long userId, @Param("imgUrl") String imgUrl);
}
