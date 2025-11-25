package com.example.api.tests;

import com.example.api.core.Env;
import com.example.api.core.Specs;
import com.example.api.domain.client.PostsApi;
import com.example.api.domain.model.Post;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class PostsGetTests extends TestBase {

    private final PostsApi postsApi = new PostsApi();

    @Test
    @DisplayName("GET /posts — returns non-empty list of posts matching schema")
    void getAllPosts_shouldReturnNonEmptyListMatchingSchema() {
        Response response = postsApi.getAllPosts();

        response.then()
                .spec(Specs.jsonOkSpec())
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/posts-array.json"));

        Post[] posts = response.as(Post[].class);

        assertThat(posts)
                .as("posts list should not be empty")
                .isNotEmpty();
    }

    @ParameterizedTest(name = "GET /posts?userId={0} — returns posts only for this userId")
    @ValueSource(ints = {1, 5, 10})
    void getPostsByUserId_shouldReturnOnlyPostsWithGivenUserId(int userId) {
        Response response = postsApi.getPostsByUserId(userId);

        response.then()
                .spec(Specs.jsonOkSpec())
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/posts-array.json"));

        Post[] posts = response.as(Post[].class);

        assertThat(posts)
                .as("posts should not be empty for userId=" + userId)
                .isNotEmpty();

        assertThat(posts)
                .as("all posts should belong to userId=" + userId)
                .allMatch(p -> p.getUserId() == userId);
    }

    @ParameterizedTest(name = "GET /posts/{0} — returns 200 and post matching schema for valid id")
    @ValueSource(ints = {1, 50, 100})
    void getPostById_shouldReturnPostForExistingId(int id) {
        Response response = postsApi.getPostById(id);

        response.then()
                .spec(Specs.jsonOkSpec())
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/post.json"));

        Post post = response.as(Post.class);

        assertThat(post.getId())
                .as("returned post id should match requested id")
                .isEqualTo(id);
    }

    @ParameterizedTest(name = "GET /posts/{0} — returns 404 or empty object for non-existing id")
    @ValueSource(ints = {0, -1, 150})
    void getPostById_shouldReturnNotFoundOrEmptyForNonExistingId(int id) {
        Response response = postsApi.getPostById(id);

        // В JSONPlaceholder для несуществующего id поведение может быть разным:
        // либо 404, либо 200 и пустой объект {}. Оставляем гибкую проверку.
        int statusCode = response.getStatusCode();
        String bodyAsString = response.getBody().asString();

        assertThat(statusCode == 404 || "{}".equals(bodyAsString.trim()))
                .as("for non-existing id we expect either 404 or empty JSON object")
                .isTrue();
    }

    @Test
    @DisplayName("GET /posts — response headers and time are within expected limits")
    void getAllPosts_headersAndResponseTimeAreValid() {
        long start = System.currentTimeMillis();

        Response response = postsApi.getAllPosts();

        long duration = System.currentTimeMillis() - start;

        response.then()
                .spec(Specs.jsonOkSpec());

        assertThat(response.getHeader("Content-Type"))
                .as("Content-Type should be application/json")
                .startsWith("application/json");

        assertThat(duration)
                .as("response time should be less than configured maxResponseTimeMs")
                .isLessThanOrEqualTo(Env.maxResponseTimeMs());
    }
}
