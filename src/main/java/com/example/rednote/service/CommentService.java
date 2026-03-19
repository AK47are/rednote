package com.example.rednote.service;

import java.util.List;

import com.example.rednote.model.dto.CommentDTO;
import com.example.rednote.model.vo.CommentVO;

public interface CommentService {

    List<CommentVO> listByPostId(Integer postId);

    void insert(CommentDTO commentDTO);

    void removeById(Integer id);
}
