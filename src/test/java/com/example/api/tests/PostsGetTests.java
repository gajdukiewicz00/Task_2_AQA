package com.example.api.tests;

import com.example.api.domain.client.PostsApi;
import com.example.api.domain.model.Post;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;


@Tag("api")
class PostsGetTests extends TestBase {

    private final PostsApi posts = new PostsApi();

    @Test
    @DisplayName("GET /posts — базовая проверка, JSON-schema, не пустой список")
    void getAllPosts_basicContract() {
        posts.getAllPosts()
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/posts-array.json"));
    }

    @ParameterizedTest(name = "GET /posts?userId={0} — все элементы имеют userId={0}")
    @ValueSource(ints = {1, 5, 10})
    void getPostsByUserId_filterWorks(int userId) {
        Response resp = posts.getPostsByUserId(userId);
        Post[] items = resp.as(Post[].class);

        assertThat(items).isNotEmpty();
        assertThat(Arrays.stream(items).map(Post::getUserId))
                .allMatch(u -> u != null && u == userId);
    }

    @ParameterizedTest(name = "GET /posts/{0} — валидный id возвращает объект c id={0}")
    @ValueSource(ints = {1, 50, 100})
    void getPostById_validIds(int id) {
        posts.getPostById(id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("userId", greaterThanOrEqualTo(1))
                .body("title", allOf(notNullValue(), instanceOf(String.class), not(isEmptyString())))
                .body("body",  allOf(notNullValue(), instanceOf(String.class), not(isEmptyString())))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/post.json"));
    }

    @ParameterizedTest(name = "GET /posts/{0} — non-existing -> 404 или пустой JSON")
    @ValueSource(ints = {0, 99999})
        void getPostById_nonExisting_returnsNotFoundOrEmpty(int id) {
        Response resp = posts.getPostById(id);
        int status = resp.statusCode();
        String body = resp.getBody().asString().trim();
        assertThat(status).isIn(404, 200);
        if (status == 200) {
            assertThat(body).isEqualTo("{}");
        } else {
            assertThat(body).isIn("", "{}", "{\"error\":\"Not Found\"}");
            String ct = resp.getHeader("Content-Type");
            if (ct != null) {
                assertThat(ct).startsWith("application/json");
            }
        }
    }

    @Test
    @DisplayName("GET /posts — заголовки и тайминги (content-type, x-powered-by, SLA)")
    void getAllPosts_headersAndTiming() {
        posts.getAllPosts()
                .then()
                .statusCode(200)
                .header("Content-Type", startsWith("application/json"))
                .header("x-powered-by", not(blankOrNullString()))
                .time(lessThan(2000L));
    }
}
