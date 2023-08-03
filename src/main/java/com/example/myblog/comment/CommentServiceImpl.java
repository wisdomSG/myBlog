package com.example.myblog.comment;

import com.example.myblog.entity.Comment;
import com.example.myblog.entity.Post;
import com.example.myblog.entity.User;
import com.example.myblog.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final PostService postService;
    @Override
    public CommentResponseDto createComment(User user, CommentRequestDto dto) {
        Post post = postService.findPost(dto.getPostId());
        Comment comment = new Comment(dto.getBody(), post, user);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(Long id, User user, CommentRequestDto dto) {

        Comment comment = findComment(id);

        comment.setBody(dto.getBody());

        return new CommentResponseDto(comment);

    }

    @Override
    public void deleteComment(Long id, User user) {

        Comment comment = findComment(id);

        commentRepository.delete(comment);
    }

    @Override
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("댓글을 찾을 수 없습니다.")
        );
    }
}
