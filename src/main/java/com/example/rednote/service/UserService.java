package com.example.rednote.service;

import com.example.rednote.model.dto.UserDTO;
import com.example.rednote.model.vo.UserVO;

public interface UserService {

    UserVO getById(Integer userId);

    Integer register(UserDTO userDTO);
}
