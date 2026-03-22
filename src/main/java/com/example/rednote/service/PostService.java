package com.example.rednote.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.rednote.model.dto.PostDTO;
import com.example.rednote.model.po.PostPO;
import com.example.rednote.model.vo.PostWithUserVO;

import java.util.List;

public interface PostService extends IService<PostPO> {
    List<PostWithUserVO> listWithUserInfo(Integer topicId);

    List<PostWithUserVO> listWithUserInfoByUserId(Integer userId);

    void addPost(PostDTO postDTO);
}
