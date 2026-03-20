package com.example.rednote.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String username;
    private String avatar;
    private String phone;
    private String email;
    private Integer gender;
    private String bio;

}
