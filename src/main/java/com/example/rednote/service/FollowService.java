package com.example.rednote.service;

import com.example.rednote.model.vo.UserVO;

import java.util.List;

public interface FollowService {

    void toogleFollow(Integer userId);

    Boolean isFollow(Integer userId);

    List<UserVO> listFollowing();
}
