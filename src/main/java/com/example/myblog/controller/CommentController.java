package com.example.myblog.controller;

import com.example.myblog.dto.ApiResponseDto;
import com.example.myblog.dto.CommentRequestDto;
import com.example.myblog.dto.CommentResponseDto;
import com.example.myblog.dto.PostResponseDto;
import com.example.myblog.security.UserDetailsImpl;
import com.example.myblog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto dto) {
        CommentResponseDto result = commentService.createComment(userDetails.getUser(), dto);

        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto dto) {
        CommentResponseDto result = commentService.updateComment(commentId, userDetails.getUser(), dto);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto> deleteCommnet(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("댓글이 삭제되었습니다.", HttpStatus.OK.value()));
    }

}
