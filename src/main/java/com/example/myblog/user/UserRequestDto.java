package com.example.myblog.user;

import com.example.myblog.entity.UserRoleEnum;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$",
            message = "username은 최소 3자 이상, 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야 합니다.")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}:\"<>?,.\\\\/]{4,}$",
            message = "최소 4자 이상이며, 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자 로 구성되어야 합니다.")
    private String password;

    private String passwordCheck;

    private UserRoleEnum role;

}
