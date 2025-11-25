package com.example.api.domain.client;

import com.example.api.core.Specs;
import com.example.api.domain.model.Post;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PostsApi {

    private static final String POSTS_PATH = "/posts";
    private static final int STATUS_CREATED = 201; // HTTP 201 Created

    public Response getAllPosts() {
        return given()
                .spec(Specs.requestSpec())
                .when()
                .get(POSTS_PATH);
    }

    public Response getPostById(int id) {
        return given()
                .spec(Specs.requestSpec())
                .when()
                .get(POSTS_PATH + "/" + id);
    }

    public Response getPostsByUserId(int userId) {
        return given()
                .spec(Specs.requestSpec())
                .queryParam("userId", userId)
                .when()
                .get(POSTS_PATH);
    }

    public Response createPost(Post post) {
        return given()
                .spec(Specs.requestSpec())
                .body(post)
                .when()
                .post(POSTS_PATH)
                .then()
                .statusCode(STATUS_CREATED)
                .extract()
                .response();
    }
}
