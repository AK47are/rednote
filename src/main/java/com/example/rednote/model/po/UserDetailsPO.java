package com.example.rednote.model.po;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@TableName("user_details")
public class UserDetailsPO {

    @TableId(type = IdType.AUTO)
    private Integer userDetailsId;
    private Integer userId;
    private String phone;
    private String email;
    private Integer gender;
    private String bio;
    private Integer followCount;
    private Integer fansCount;
    private Integer postCount;
    private Integer likeReceiveCount;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
