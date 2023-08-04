package com.example.myblog.post;

import com.example.myblog.awsS3.AwsS3Service;
import com.example.myblog.entity.Post;
import com.example.myblog.entity.PostImage;
import com.example.myblog.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final AwsS3Service awsS3Service;
    private final PostImageRepository postImageRepository;


    @Override
    public PostResponseDto createPost(PostRequestDto dto, User user,  List<MultipartFile> multipartFiles) {
        // Aws에 이미지 저장
        List<String> imgPaths = awsS3Service.uploadFile(multipartFiles);

        String title = dto.getTitle();
        String content = dto.getContent();

        Post post = new Post(title, content, user);
        postRepository.save(post);

        List<String> imgList = new ArrayList<>();
        for (String imgUrl : imgPaths) {
            PostImage img = new PostImage(imgUrl, post);
            postImageRepository.save(img);
            imgList.add(img.getFileName());
        }

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
    public PostListResponseDto getPostListWithPage(Pageable pageable) {


        List<PostResponseDto> postList = postRepository.getPostListWithPage(pageable)
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
    public PostListResponseDto getPostFindByTitleList(String keyword) {
        List<PostResponseDto> postList = postRepository.getPostFindByTitleList(keyword)
                .stream()
                .map(PostResponseDto::new)
                .toList();
        return new PostListResponseDto(postList);
    }

    @Override
    @Transactional //
    public PostResponseDto updatePost(Long id, User user, PostRequestDto dto) {
        Post post = findPost(id);

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return new PostResponseDto(post);
    }

    @Override
    public void deletePost(Long id, User user) throws IOException {
        Post post = findPost(id);

        // 등록된 이미지 삭제
        List<String> imgPaths = new ArrayList<>();
        for(PostImage postImage : post.getPostImageList()) {
            imgPaths.add(postImage.getFileName());
        }
        awsS3Service.deleteFile(imgPaths);

        postRepository.delete(post);
    }

    @Override
    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("게시물을 찾을 수 없습니다.")
        );
    }
}
