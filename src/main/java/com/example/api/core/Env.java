package com.example.api.core;

public final class Env {
    private Env() {}

    public static String baseUrl() {
        String url = System.getProperty("baseUrl", "https://jsonplaceholder.typicode.com");
        if (url.endsWith("/")) url = url.substring(0, url.length() - 1);
        return url;
    }

    public static long maxResponseTimeMs() {
        return Long.getLong("maxResponseTimeMs", 2000L);
    }
}
