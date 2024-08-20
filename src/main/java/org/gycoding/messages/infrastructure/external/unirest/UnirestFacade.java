package org.gycoding.messages.infrastructure.external.unirest;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Map;

public class UnirestFacade {

    public static HttpResponse<String> get(String url) {
        return Unirest.get(url)
                .header("content-type", "application/json")
                .asString();
    }

    public static HttpResponse<String> get(String url, Map<String, String> headers) {
        return Unirest.get(url)
                .headers(headers)
                .asString();
    }

    public static HttpResponse<String> post(String url) {
        return Unirest.post(url)
                .header("content-type", "application/json")
                .asString();
    }

    public static HttpResponse<String> post(String url, String body) {
        return Unirest.post(url)
                .header("content-type", "application/json")
                .body(body)
                .asString();
    }

    public static HttpResponse<String> post(String url, Map<String, String> headers, String body) {
        return Unirest.post(url)
                .headers(headers)
                .body(body)
                .asString();
    }

    public static HttpResponse<String> put(String url, Map<String, String> headers, String body) {
        return Unirest.put(url)
                .headers(headers)
                .body(body)
                .asString();
    }

    public static HttpResponse<String> delete(String url, Map<String, String> headers, String body) {
        return Unirest.delete(url)
                .headers(headers)
                .body(body)
                .asString();
    }
}
