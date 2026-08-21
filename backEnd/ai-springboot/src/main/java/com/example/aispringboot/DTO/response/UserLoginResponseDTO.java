package com.example.aispringboot.DTO.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserLoginResponseDTO {
    private String token;
    private String roleType;
    private UserDetailResponseDTO userInfo;

    public static class UserDetailResponseDTO{
        private Long id;
        private String username;
        private String email;
        private String avatar;
        private String phone;
        private Integer gender;
        private String genderDisplayName;
        private String birthday;
        private String userType;
        private String userTypeDisplayName;
        private Integer status;
        private String statusDisplayName;
        private String displayName;
        private String createdAt;
        private String updatedAt;
    }
}

