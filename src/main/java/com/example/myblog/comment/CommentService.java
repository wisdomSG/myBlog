package com.example.myblog.comment;

import com.example.myblog.dto.CommentRequestDto;
import com.example.myblog.dto.CommentResponseDto;
import com.example.myblog.entity.Comment;
import com.example.myblog.entity.User;

public interface CommentService {
    /**
     * 댓글 생성
     * @param user 댓글 생성 요청자
     * @param dto 댓글 생성 요청 정보
     * @return 생성한 댓글 정보
     */
    CommentResponseDto createComment(User user, CommentRequestDto dto);

    /**
     * 댓글 수정
     * @param id 수정할 댓글 id
     * @param user 댓글 수정 요청자
     * @param dto 댓글 수정 요청 정보
     * @return 수정한 댓글 정보
     */
    CommentResponseDto updateComment(Long id, User user, CommentRequestDto dto);

    /**
     * 댓글 삭제
     * @param id 삭제할 댓글 id
     * @param user 댓글 삭제 요청자
     */
    void deleteComment(Long id, User user);

    /**
     *
     * @param id
     * @return
     */
    Comment findComment(Long id);
}
