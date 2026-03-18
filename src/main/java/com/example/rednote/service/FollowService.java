package com.example.rednote.service;

public interface FollowService {

    void toogleFollow(Integer userId);

    Boolean isFollow(Integer userId);

}
