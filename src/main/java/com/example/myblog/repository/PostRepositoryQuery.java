package com.example.myblog.repository;

import com.example.myblog.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepositoryQuery {

    List<Post> getPostFindByTitleList (String keyword);

    List<Post> getPostListWithPage (Long offset, int pageSize);
}
