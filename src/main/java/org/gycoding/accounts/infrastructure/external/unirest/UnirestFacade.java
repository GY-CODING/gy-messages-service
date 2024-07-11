package org.gycoding.accounts.infrastructure.external.unirest;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

/**
 * Facade for Unirest library. It's purpose is to make HTTP requests.
 * @see Unirest
 * @author Ivan Vicente Morales (<a href="https://toxyc.dev/">ToxYc</a>)
 */
public class UnirestFacade {
    /**
     * Sends a GET request to the specified URL.
     * @param url
     * @see HttpResponse
     * @author Ivan Vicente Morales (<a href="https://toxyc.dev/">ToxYc</a>)
     */
    public static HttpResponse<String> get(String url) {
        return Unirest.get(url)
                .header("content-type", "application/json")
                .asString();
    }

    /**
     * Sends a POST without body request to the specified URL.
     * @param url
     * @see HttpResponse
     * @author Ivan Vicente Morales (<a href="https://toxyc.dev/">ToxYc</a>)
     */
    public static HttpResponse<String> post(String url) {
        return Unirest.post(url)
                .header("content-type", "application/json")
                .asString();
    }

    /**
     * Sends a POST with body request to the specified URL.
     * @param url
     * @see HttpResponse
     * @author Ivan Vicente Morales (<a href="https://toxyc.dev/">ToxYc</a>)
     */
    public static HttpResponse<String> post(String url, String body) {
        return Unirest.post(url)
                .header("content-type", "application/json")
                .body(body)
                .asString();
    }
}
