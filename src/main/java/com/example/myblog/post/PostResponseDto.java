package com.example.myblog.post;

import com.example.myblog.comment.CommentResponseDto;
import com.example.myblog.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String createdAt;
    private String modifiedAt;
    private List<CommentResponseDto> commentList;
    private List<PostImageResponseDto> postImageList = new ArrayList<>();
    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAtFormatted();
        this.modifiedAt = post.getModifiedAtFormatted();
        this.commentList = post.getCommentList()
                .stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed()) // 작성날짜 내림차순
                .toList();
    }
}
