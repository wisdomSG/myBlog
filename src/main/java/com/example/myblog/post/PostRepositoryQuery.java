package com.example.myblog.post;

import com.example.myblog.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryQuery {

    List<Post> getPostFindByTitleList (String keyword);

    List<Post> getPostListWithPage (Pageable pageable);
}
