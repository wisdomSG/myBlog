package com.example.myblog.aop;

import com.example.myblog.entity.Comment;
import com.example.myblog.entity.Post;
import com.example.myblog.entity.User;
import com.example.myblog.entity.UserRoleEnum;
import com.example.myblog.comment.CommentService;
import com.example.myblog.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.RejectedExecutionException;

@Slf4j(topic = "RoleCheckAop")
@Component
@Aspect // AOP를 가능하게하는 어노테이션
public class RoleCheckAop {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Pointcut("execution(* com.example.myblog.post.PostService.updatePost(..))") //AOP의 어느 지점에서 실행될지를 결정
    private void updatePost() {}

    @Pointcut("execution(* com.example.myblog.post.PostService.deletePost(..))") //AOP의 어느 지점에서 실행될지를 결정
    private void deletePost() {}

    @Pointcut("execution(* com.example.myblog.comment.CommentService.updateComment(..))") //AOP의 어느 지점에서 실행될지를 결정
    private void updateComment() {}

    @Pointcut("execution(* com.example.myblog.comment.CommentService.deleteComment(..))") //AOP의 어느 지점에서 실행될지를 결정
    private void deleteComment() {}

    @Around("updatePost() || deletePost()")
    public Object executePostRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1,2번째 매개변수로 id, user값 가져오기
        Long id = (Long) joinPoint.getArgs()[0];
        User user = (User) joinPoint.getArgs()[1];

        // 타겟 메서드에서 post 객체 가져오기
        Post post = postService.findPost(id);

        // 게시글 작성자(post.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        if (user.getRole().equals(UserRoleEnum.ADMIN) || !post.getUser().equals(user)) {
            log.warn("[AOP] 작성자만 게시글을 수정/삭제 할 수 있습니다.");
            throw new RejectedExecutionException();
        }

        //핵심기능 수행
        return joinPoint.proceed();
    }

    @Around("updateComment() || deleteComment()")
    public Object excuteCommentRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1,2번째 매개변수로 id, user값 가져오기
        Long id = (Long) joinPoint.getArgs()[0];
        User user = (User) joinPoint.getArgs()[1];

        // 타겟 메서드에서 comment 객체 가져오기
        Comment comment = commentService.findComment(id);

        // 댓글 작성자(comment.user) 와 요청자(user) 가 같은지 또는 Admin 인지 체크 (아니면 예외발생)
        if(user.getRole().equals(UserRoleEnum.ADMIN) || !comment.getUser().equals(user)) {
            log.warn("[AOP] 작성자만 댓글을 수정/삭제 할 수 있습니다.");
            throw new RejectedExecutionException();
        }

        //핵심기능 수행
        return joinPoint.proceed();
    }
}
