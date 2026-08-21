package com.example.aispringboot.DTO.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserLoginCommandDTO {
    @NotBlank(message = "用户名或邮箱不能为空")
    @Size(max = 100,message = "用户名或邮箱长度不能超过100")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(max = 50,min = 6, message = "密码长度不能小于6或超过50")
    private String password;
}
