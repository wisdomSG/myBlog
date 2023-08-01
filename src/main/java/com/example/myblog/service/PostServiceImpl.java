package com.example.myblog.service;

import com.example.myblog.dto.PostListResponseDto;
import com.example.myblog.dto.PostRequestDto;
import com.example.myblog.dto.PostResponseDto;
import com.example.myblog.entity.Post;
import com.example.myblog.entity.User;
import com.example.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public PostResponseDto createPost(PostRequestDto dto, User user) {
        String title = dto.getTitle();
        String content = dto.getContent();

        Post post = new Post(title, content, user);
        postRepository.save(post);

        return new PostResponseDto(post);
    }

    @Override
    public PostListResponseDto getAllPosts() {
        List<PostResponseDto> postList = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(PostResponseDto::new)
                .toList();

        return new PostListResponseDto(postList);
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = findPost(id);

        return new PostResponseDto(post);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, User user, PostRequestDto dto) {
        Post post = findPost(id);

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return new PostResponseDto(post);
    }

    @Override
    public void deletePost(Long id, User user) {
        Post post = findPost(id);

        postRepository.delete(post);
    }

    @Override
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );
    }
}
