package com.example.rednote.service;

import com.example.rednote.model.dto.UserAuthDTO;
import com.example.rednote.model.vo.UserVO;

public interface UserService {

    UserVO getById(Integer userId);

    void register(UserAuthDTO userAuthDTO);

    String login(UserAuthDTO userAuthDTO);

    UserVO getCurrentUser();
}
