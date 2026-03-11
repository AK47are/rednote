package com.example.rednote.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rednote.mapper.PostMapper;
import com.example.rednote.model.po.PostPO;
import com.example.rednote.service.PostService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostServiceImpl extends ServiceImpl<PostMapper, PostPO> implements PostService {
}
