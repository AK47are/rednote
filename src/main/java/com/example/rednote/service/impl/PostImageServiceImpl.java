package com.example.rednote.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.rednote.common.utils.MinioUtils;
import com.example.rednote.mapper.PostImageMapper;
import com.example.rednote.model.po.PostImagePO;
import com.example.rednote.service.PostImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class PostImageServiceImpl implements PostImageService {

    private final PostImageMapper postImageMapper;
    private final MinioUtils minioUtils;

    @Override
    public List<String> list(Integer postId) {
        List<PostImagePO> postImagePOS = postImageMapper
                .selectList(new LambdaQueryWrapper<PostImagePO>().eq(PostImagePO::getPostId, postId));
        return postImagePOS.stream().map(postImagePO -> {
            postImagePO.setUrl(minioUtils.getPublicUrl(postImagePO.getUrl()));
            return postImagePO.getUrl();
        }).toList();
    }
}
