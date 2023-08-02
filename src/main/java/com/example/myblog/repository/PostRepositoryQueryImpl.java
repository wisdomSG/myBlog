package com.example.myblog.repository;

import com.example.myblog.entity.Post;
import com.example.myblog.entity.QPost;
import com.example.myblog.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;



import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getPostFindByTitleList(String keyword) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        return jpaQueryFactory.selectFrom(post)
                .leftJoin(post.user, user)
                .where(post.title.like("%" + keyword + "%"))
                .fetch();
    }

    @Override
    public List<Post> getPostListWithPage(Pageable pageable) {
        QPost post = QPost.post;

        return jpaQueryFactory.selectFrom(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
