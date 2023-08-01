package com.example.myblog.controller;

import com.example.myblog.dto.ApiResponseDto;
import com.example.myblog.dto.PostListResponseDto;
import com.example.myblog.dto.PostRequestDto;
import com.example.myblog.dto.PostResponseDto;
import com.example.myblog.security.UserDetailsImpl;
import com.example.myblog.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostServiceImpl postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto dto) {
        PostResponseDto result = postService.createPost(dto, userDetails.getUser());

        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public ResponseEntity<PostListResponseDto> getAllPosts() {
        PostListResponseDto result = postService.getAllPosts();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@RequestBody PostRequestDto dto,@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto result;
        try {
            result = postService.updatePost(id, userDetails.getUser(), dto);
        } catch (RejectedExecutionException e) {
            throw new RuntimeException("작성자만 수정할수 있습니다.", e);
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.deletePost(id, userDetails.getUser());
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.ok().body(new ApiResponseDto("게시물이 삭제되었습니다.", HttpStatus.CREATED.value()));
    }
}
