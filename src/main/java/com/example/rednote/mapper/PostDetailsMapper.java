package com.example.rednote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.rednote.model.po.PostDetailsPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostDetailsMapper extends BaseMapper<PostDetailsPO> {
    @Update("UPDATE post_details SET like_count = like_count + #{delta} WHERE post_id = #{postId}")
    void updateLikeCount(@Param("postId") Integer postId, @Param("delta") int delta);
}
