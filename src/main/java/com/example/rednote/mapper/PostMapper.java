package com.example.rednote.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rednote.model.po.PostPO;

@Mapper
public interface PostMapper extends BaseMapper<PostPO> {

}
