package com.example.rednote.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("topic")
public class TopicPO {
    @TableId(type = IdType.AUTO)
    private Integer topicId;

    private String topicName;
    private String topicDescription;
    private String topicIcon;
    private String cover;
    private Integer postCount;
    private Integer followCount;
    private Boolean isHot;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
