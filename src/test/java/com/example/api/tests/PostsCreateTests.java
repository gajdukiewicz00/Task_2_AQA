package com.example.api.tests;

import com.example.api.domain.client.PostsApi;
import com.example.api.domain.model.Post;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
class PostsCreateTests extends TestBase {

    private final PostsApi posts = new PostsApi();

    @Test
    @DisplayName("POST /posts — позитив: создаёт пост, возвращает 201 и эхо-поля + новый id")
    void createPost_positive() {
        Post payload = new Post(1, "foo", "bar");

        Response resp = posts.createPost(payload);
        Post created = resp.as(Post.class);

        assertThat(resp.statusCode()).isEqualTo(201);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getUserId()).isEqualTo(1);
        assertThat(created.getTitle()).isEqualTo("foo");
        assertThat(created.getBody()).isEqualTo("bar");
    }

    @Test
    @DisplayName("POST /posts — «негатив»: пустой title допускается и просто эхоится (особенность сервиса)")
    void createPost_emptyTitle_echoed() {
        Post payload = new Post(2, "", "content");

        Response resp = posts.createPost(payload);
        Post created = resp.as(Post.class);

        assertThat(resp.statusCode()).isEqualTo(201);
        assertThat(created.getUserId()).isEqualTo(2);
        assertThat(created.getTitle()).isEqualTo("");
        assertThat(created.getBody()).isEqualTo("content");
    }

    @Test
    @DisplayName("POST /posts — «негатив»: отсутствует body => сервер просто вернёт то, что прислали")
    void createPost_missingBodyField() {
        Post payload = new Post(3, "title", null);

        Response resp = posts.createPost(payload);
        Post created = resp.as(Post.class);

        assertThat(resp.statusCode()).isEqualTo(201);
        assertThat(created.getBody()).isNull();
        assertThat(created.getTitle()).isEqualTo("title");
        assertThat(created.getUserId()).isEqualTo(3);
    }
}
