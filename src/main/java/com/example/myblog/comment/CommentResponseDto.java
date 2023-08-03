package com.example.myblog.comment;

import com.example.myblog.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {
    private String body;
    private String username;
    private String createdAt;
    private String modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.body = comment.getBody();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAtFormatted();
        this.modifiedAt = comment.getModifiedAtFormatted();
    }
}
