package org.gycoding.messages.domain.entities;

import lombok.Builder;

import java.util.Map;

@Builder
public record Member(
        String userId
) {
    @Override
    public String toString() {
        return "{" +
                "\"userId\": \"" + userId + "\"" +
                "}";
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "userId", userId
        );
    }
}