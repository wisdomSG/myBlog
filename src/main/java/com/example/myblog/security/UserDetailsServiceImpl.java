package com.example.myblog.security;

import com.example.myblog.entity.User;
import com.example.myblog.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 2. 인증정보 받아오기
 * UsernamePasswordAuthenticationFilter > UserDetailsService 구현 > loadUserByUsername() > UserDetails > Authentication (createSuccessAuthentication()에서 만들어짐)
 */
@Service
@RequiredArgsConstructor // 생성자(final)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        return new UserDetailsImpl(user);
    }
}
