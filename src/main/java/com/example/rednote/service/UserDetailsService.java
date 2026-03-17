package com.example.rednote.service;

import com.example.rednote.model.po.UserDetailsPO;

public interface UserDetailsService {

    UserDetailsPO getById(Integer userId);

}
