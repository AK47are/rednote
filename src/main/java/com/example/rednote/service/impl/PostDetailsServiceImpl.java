package com.example.rednote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.rednote.mapper.PostDetailsMapper;
import com.example.rednote.model.po.PostDetailsPO;
import com.example.rednote.service.PostDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostDetailsServiceImpl extends ServiceImpl<PostDetailsMapper, PostDetailsPO>
        implements PostDetailsService {
}
