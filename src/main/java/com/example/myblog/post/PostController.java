package com.example.myblog.post;

import com.example.myblog.dto.ApiResponseDto;
import com.example.myblog.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "PostController",description="게시물 API")
public class PostController {
    private final PostServiceImpl postService;

    @PostMapping
    @Operation(summary = "게시물 저장 API")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("requestDto") PostRequestDto dto, @RequestPart("fileName") List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) {
            throw new IllegalArgumentException("파일 업로드 필수");
        }

        PostResponseDto result = postService.createPost(dto, userDetails.getUser(), multipartFiles);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    @Operation(summary = "게시물 전체 조회 API")
    public ResponseEntity<PostListResponseDto> getAllPosts() {
        PostListResponseDto result = postService.getAllPosts();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/page")
    @Operation(summary = "게시물 전체 조회 (페이징 정렬) API")
    public ResponseEntity<PostListResponseDto> getPostListWithPage(Pageable pageable) {
        PostListResponseDto result = postService.getPostListWithPage(pageable);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/search")
    @Operation(summary = "게시물 제목 검색 API")
    public ResponseEntity<PostListResponseDto> getPostFindByTitleList(@RequestParam("keyword") String keyword) {
        PostListResponseDto result = postService.getPostFindByTitleList(keyword);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시물 단건 조회 API")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시물 수정 API")
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
    @Operation(summary = "게시물 삭제 API")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.deletePost(id, userDetails.getUser());
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto("작성자만 삭제할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(new ApiResponseDto("게시물이 삭제되었습니다.", HttpStatus.CREATED.value()));
    }
}
