package com.example.myblog.post;

import com.example.myblog.entity.Post;
import com.example.myblog.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {

    /**
     * 게시물 생성
     * @param dto 게시물 생성 요청 정보
     * @param user 게시물 생성 요청자
     * @return 게시물 생성 결과
     */

    /**
     * 게시물 생성
     * @param dto 게시물 생성 요청 정보
     * @param user 게시물 생성 요청자
     * @param multipartFiles 게시물 이미지
     * @return 게시물 생성 결과
     */
    PostResponseDto createPost(PostRequestDto dto, User user, List<MultipartFile>multipartFiles);

    /**
     * 게시물 전체 조회
     * @return 게시물 전체 목록
     */
    PostListResponseDto getAllPosts();

    /**
     * 게시물 페이징 정렬 조회
     * @param pageable
     * @return
     */
    PostListResponseDto getPostListWithPage(Pageable pageable);

    /**
     * 게시물 Id 별 조회
     * @param id 조회할 게시물 Id
     * @return 조회된 게시물
     */
    PostResponseDto getPostById(Long id);

    /**
     * 게시물 제목 검색 기능
     * @param keyword 제목 Keyword
     * @return 조회된 게시물
     */
    PostListResponseDto getPostFindByTitleList(String keyword);

    /**
     * 게시물 수정
     * @param id 수정할 게시물 Id
     * @param user 게시물 수정 요청자
     * @param dto 게시물 수정 요청 정보
     * @return 게시물 수정 결과
     */
    PostResponseDto updatePost(Long id, User user, PostRequestDto dto);

    /**
     * 게시물 삭제
     * @param id 삭제할 게시물 Id
     * @param user 게시물 삭제 요청자
     */
    void deletePost(Long id, User user) throws IOException;

    /**
     * Id로 게시물 검색
     * @param id 찾을 게시물 Id
     * @return 찾은 게시물
     */
    Post findPost(Long id);
}
