package org.gycoding.accounts.domain.entities;

import lombok.Builder;
import org.gycoding.accounts.domain.enums.MessageStates;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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