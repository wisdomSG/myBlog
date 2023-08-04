package com.example.myblog.controller;

import com.example.myblog.config.WebSecurityConfig;
import com.example.myblog.post.PostRequestDto;
import com.example.myblog.entity.User;
import com.example.myblog.entity.UserRoleEnum;
import com.example.myblog.mvc.MockSpringSecurityFilter;
import com.example.myblog.post.PostController;
import com.example.myblog.security.UserDetailsImpl;
import com.example.myblog.post.PostService;
import com.example.myblog.user.UserController;
import com.example.myblog.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {UserController.class, PostController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class PostControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean // 껍데기만 만들겠다는 뜻  (사전정의 하는 방법이 있음)
    PostService postService;

    private static final String BASE_URL = "/api";

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String username = "soll435";
        String password = "123456";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(username, password, role);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }
    @Test
    @DisplayName("게시물 저장 테스트")
    void save_test() throws Exception {
        //given
        this.mockUserSetup();
        String title = "Test title";
        String content = "Test content";

        //when
        String body = objectMapper.writeValueAsString(
                PostRequestDto.builder().content(content).title(title).build()
        );
        //then
        mvc.perform(post(BASE_URL + "/posts")
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

    }
}