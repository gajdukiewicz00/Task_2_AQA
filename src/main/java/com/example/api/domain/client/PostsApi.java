package com.example.api.domain.client;

import com.example.api.core.Specs;
import com.example.api.domain.model.Post;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class PostsApi {
    private static final String POSTS = "/posts";

    public Response getAllPosts() {
        return given().spec(Specs.requestSpec())
                .when().get(POSTS)
                .then().spec(Specs.jsonOkSpec())
                .extract().response();
    }

    public Response getPostById(int id) {
        return given().spec(Specs.requestSpec())
                .pathParam("id", id)
                .when().get(POSTS + "/{id}")
                .then().spec(Specs.jsonOkSpec())
                .extract().response();
    }

    public Response getPostsByUserId(int userId) {
        return given().spec(Specs.requestSpec())
                .queryParam("userId", userId)
                .when().get(POSTS)
                .then().spec(Specs.jsonOkSpec())
                .extract().response();
    }

    public Response createPost(Post post) {
        return given().spec(Specs.requestSpec())
                .body(post)
                .when().post(POSTS)
                .then()
                .statusCode(201)
                .extract().response();
    }
}
