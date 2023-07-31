package com.example.myblog.service;

import com.example.myblog.dto.UserRequestDto;
import com.example.myblog.entity.User;
import com.example.myblog.entity.UserRoleEnum;
import com.example.myblog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    public void signup(UserRequestDto dto) {
        String username = dto.getUsername();
        String password = passwordEncoder.encode(dto.getPassword());
        String passwordCheck = dto.getPasswordCheck();
        UserRoleEnum role = dto.getRole();

        if(userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        if(!passwordEncoder.matches(passwordCheck, password)) {
            throw new IllegalArgumentException("입력하신 비밀번호가 일치하지 않습니다.");
        }

        User user = new User(username, password, role);

        userRepository.save(user);

    }
}
