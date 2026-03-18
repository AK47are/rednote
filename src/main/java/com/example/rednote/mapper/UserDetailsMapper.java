package com.example.rednote.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rednote.model.po.UserDetailsPO;

@Mapper
public interface UserDetailsMapper extends BaseMapper<UserDetailsPO> {

    @Update("UPDATE user_details SET follow_count = follow_count + #{delta} WHERE user_id = #{userId}")
    void updateFollowCount(@Param("userId") Integer userId, @Param("delta") int delta);

    @Update("UPDATE user_details SET fans_count = fans_count + #{delta} WHERE user_id = #{userId}")
    void updateFansCount(@Param("userId") Integer userId, @Param("delta") int delta);

    @Update("UPDATE user_details SET post_count = post_count + #{delta} WHERE user_id = #{userId}")
    void updatePostCount(@Param("userId") Integer userId, @Param("delta") int delta);

    @Update("UPDATE user_details SET like_receive_count = like_receive_count + #{delta} WHERE user_id = #{userId}")
    void updateLikeReceiveCount(@Param("userId") Integer userId, @Param("delta") int delta);

}
