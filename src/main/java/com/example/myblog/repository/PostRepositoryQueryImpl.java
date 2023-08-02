package com.example.myblog.repository;

import com.example.myblog.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getPostFindByTitleList(String keyword) {
        return null;
    }

    @Override
    public List<Post> getPorductListWithPage(Long offset, int pageSize) { return null;}
}
