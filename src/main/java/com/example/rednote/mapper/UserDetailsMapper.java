package com.example.rednote.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rednote.model.po.UserDetailsPO;

@Mapper
public interface UserDetailsMapper extends BaseMapper<UserDetailsPO> {

}
