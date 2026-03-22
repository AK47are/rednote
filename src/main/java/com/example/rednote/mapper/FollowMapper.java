package com.example.rednote.mapper;

import com.example.rednote.model.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rednote.model.po.FollowPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper extends BaseMapper<FollowPO> {

    List<UserVO> selectByUserId(@Param("userId") Integer userId);
}
