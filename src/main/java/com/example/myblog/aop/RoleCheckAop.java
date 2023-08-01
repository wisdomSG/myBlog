package com.example.myblog.aop;

import com.example.myblog.entity.Post;
import com.example.myblog.entity.User;
import com.example.myblog.entity.UserRoleEnum;
import com.example.myblog.security.UserDetailsImpl;
import com.example.myblog.service.PostService;
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

    @Pointcut("execution(* com.example.myblog.service.PostService.updatePost(..))") //AOP의 어느 지점에서 실행될지를 결정
    private void updatePost() {}

    @Pointcut("execution(* com.example.myblog.service.PostService.deletePost(..))") //AOP의 어느 지점에서 실행될지를 결정
    private void deletePost() {}

    @Around("updatePost() || deletePost()")
    public Object executePostRoleCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1,2번째 매개변수로 id, user값 가져오기
        Long id = (Long) joinPoint.getArgs()[0];
        User user = (User) joinPoint.getArgs()[1];

        // 타겟 메서드에서 post 객체 가져오기
        Post post = postService.findPost(id);

        //로그인한 회원 정보
        if (user.getRole().equals(UserRoleEnum.ADMIN) || !post.getUser().equals(user)) {
            log.warn("[AOP] 작성자만 게시글을 수정/삭제 할 수 있습니다.");
            throw new RejectedExecutionException();
        }

        //핵심기능 수행
        return joinPoint.proceed();
    }
}
