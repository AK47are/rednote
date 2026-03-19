package com.example.rednote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.rednote.mapper.TopicMapper;
import com.example.rednote.model.po.TopicPO;
import com.example.rednote.model.vo.TopicVO;
import com.example.rednote.service.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicMapper topicMapper;

    @Override
    public List<TopicVO> list() {
        List<TopicPO> topicPOS = topicMapper.selectList(null);
        return BeanUtil.copyToList(topicPOS, TopicVO.class);
    }
}
