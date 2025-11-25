package com.example.api.tests;

import com.example.api.domain.client.PostsApi;
import com.example.api.domain.model.Post;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostsCreateTests extends TestBase {

    private static final int STATUS_CREATED = 201; // HTTP 201 Created

    private final PostsApi postsApi = new PostsApi();

    @Test
    @DisplayName("POST /posts — positive: creates a post and returns 201 with echoed fields and new id")
    void createPost_positive() {
        Post newPost = new Post()
                .setUserId(1)
                .setTitle("Sample title")
                .setBody("Sample body");

        Response response = postsApi.createPost(newPost);

        response.then()
                .statusCode(STATUS_CREATED);

        Post createdPost = response.as(Post.class);

        Assertions.assertThat(createdPost.getId())
                .as("id should be generated for created post")
                .isNotNull();

        Assertions.assertThat(createdPost.getUserId())
                .as("userId should be echoed")
                .isEqualTo(newPost.getUserId());

        Assertions.assertThat(createdPost.getTitle())
                .as("title should be echoed")
                .isEqualTo(newPost.getTitle());

        Assertions.assertThat(createdPost.getBody())
                .as("body should be echoed")
                .isEqualTo(newPost.getBody());
    }

    @Test
    @DisplayName("POST /posts — accepts empty title and returns 201 with echoed body and userId")
    void createPost_withEmptyTitle() {
        Post newPost = new Post()
                .setUserId(1)
                .setTitle("")
                .setBody("Body with empty title");

        Response response = postsApi.createPost(newPost);

        response.then()
                .statusCode(STATUS_CREATED);

        Post createdPost = response.as(Post.class);

        Assertions.assertThat(createdPost.getTitle())
                .as("title is expected to be echoed even if it's empty")
                .isEqualTo(newPost.getTitle());

        Assertions.assertThat(createdPost.getBody())
                .as("body should be echoed")
                .isEqualTo(newPost.getBody());

        Assertions.assertThat(createdPost.getUserId())
                .as("userId should be echoed")
                .isEqualTo(newPost.getUserId());
    }

    @Test
    @DisplayName("POST /posts — accepts null body and returns 201 with echoed title and userId")
    void createPost_withNullBody() {
        Post newPost = new Post()
                .setUserId(1)
                .setTitle("Title with null body")
                .setBody(null);

        Response response = postsApi.createPost(newPost);

        response.then()
                .statusCode(STATUS_CREATED);

        Post createdPost = response.as(Post.class);

        Assertions.assertThat(createdPost.getUserId())
                .as("userId should be echoed")
                .isEqualTo(newPost.getUserId());

        Assertions.assertThat(createdPost.getTitle())
                .as("title should be echoed")
                .isEqualTo(newPost.getTitle());

        // В JSONPlaceholder поле body при null может отсутствовать или быть null —
        // оставляем гибкую проверку, как у тебя было изначально.
        Assertions.assertThat(createdPost.getBody())
                .as("body may be null or missing when sent as null")
                .isEqualTo(newPost.getBody());
    }
}
