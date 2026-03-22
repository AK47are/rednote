package com.example.rednote.mapper;

import com.example.rednote.model.vo.PostWithUserVO;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rednote.model.po.PostPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<PostPO> {
    List<PostWithUserVO> selectPostWithUser(@Param("userId") Integer userId);
}
