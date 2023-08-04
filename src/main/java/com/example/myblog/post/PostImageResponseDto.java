package com.example.myblog.post;

import com.example.myblog.entity.PostImage;
import lombok.Getter;

@Getter
public class PostImageResponseDto {
    private String fileName;

    public PostImageResponseDto(PostImage postImage) {
        this.fileName = postImage.getFileName();
    }
}
