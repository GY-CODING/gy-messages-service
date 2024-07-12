package org.gycoding.accounts.infrastructure.external.unirest;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.Map;


public class UnirestFacade {

    public static HttpResponse<String> get(String url) {
        return Unirest.get(url)
                .header("content-type", "application/json")
                .asString();
    }

    public static HttpResponse<String> get(String url, String jwt) {
        return Unirest.get(url)
                .header("content-type", "application/json")
                .header("jwt", jwt)
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

    public static HttpResponse<String> post(String url, String jwt, String body) {
        return Unirest.post(url)
                .header("content-type", "application/json")
                .header("jwt", jwt)
                .body(body)
                .asString();
    }

    public static HttpResponse<String> put(String url, String jwt, String body) {
        return Unirest.put(url)
                .header("content-type", "application/json")
                .header("jwt", jwt)
                .body(body)
                .asString();
    }

    public static HttpResponse<String> delete(String url, String userId, String body) {
        return Unirest.delete(url)
                .header("content-type", "application/json")
                .header("userId", userId)
                .body(body)
                .asString();
    }
}
