package com.example.api.core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.*;

public final class Specs {
    private Specs() {}

    public static RequestSpecification requestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(Env.baseUrl())
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .log(LogDetail.METHOD)
                .build();
    }

    public static ResponseSpecification jsonOkSpec() {
        return new ResponseSpecBuilder()
                .expectHeader("Content-Type", startsWith("application/json"))
                .expectResponseTime(lessThan(Env.maxResponseTimeMs()))
                .build();
    }
}
