package com.example.myblog.controller;

import com.example.myblog.dto.ApiResponseDto;
import com.example.myblog.dto.UserRequestDto;
import com.example.myblog.jwt.JwtUtil;
import com.example.myblog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserControlloer {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody UserRequestDto dto) {
        try {
            userService.signup(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("회원가입 실패", HttpStatus.BAD_REQUEST.value()));
        }
        return  ResponseEntity.ok().body(new ApiResponseDto("회원가입 성공", HttpStatus.CREATED.value()));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> login(@Valid @RequestBody UserRequestDto dto, HttpServletResponse response) {
        try {
            userService.login(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("회원을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST.value()));
        }

        // JWT 생성 및 헤더에 추가
        String token = jwtUtil.createToken(dto.getUsername(), dto.getRole());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return ResponseEntity.ok().body(new ApiResponseDto("로그인 성공", HttpStatus.CREATED.value()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class}) //Validation기능에서 유효성 검사를 통과하지 못한 경우에 발생하는 예외처리
    public ResponseEntity<ApiResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiResponseDto restApiException = new ApiResponseDto(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                // HTTP body
                restApiException,
                // HTTP status code
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponseDto restApiException = new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }
}
