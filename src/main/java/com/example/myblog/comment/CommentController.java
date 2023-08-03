package com.example.myblog.comment;

import com.example.myblog.dto.ApiResponseDto;
import com.example.myblog.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

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
        CommentResponseDto result = null;
        try {
            result = commentService.updateComment(commentId, userDetails.getUser(), dto);
        } catch (RejectedExecutionException e) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.", e);
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDto> deleteCommnet(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            commentService.deleteComment(commentId, userDetails.getUser());
        } catch (RejectedExecutionException e) {
            throw new RuntimeException("작성자만 삭제할 수 있습니다.", e);
        }
        return ResponseEntity.ok().body(new ApiResponseDto("댓글이 삭제되었습니다.", HttpStatus.OK.value()));
    }

}
