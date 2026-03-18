package com.example.rednote.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("post_details")
public class PostDetailsPO {
    @TableId(type = IdType.AUTO)
    private Integer postId;
    private Integer type;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer collectCount;
    // private Integer status;
    // private String auditRemark;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    // private LocalDateTime auditTime;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    // private LocalDateTime createdTime;
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    // private LocalDateTime updatedTime;
}
