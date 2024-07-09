package org.gycoding.accounts.domain.entities;

import lombok.Builder;
import org.gycoding.accounts.domain.enums.MessageStates;

import java.util.Map;

@Builder
public record Message(
        String author,
        String content,
        String date,
        MessageStates state
) {
    @Override
    public String toString() {
        return "{" +
                "\"author\": \"" + author + "\"," +
                "\"content\": \"" + content + "\"," +
                "\"date\": \"" + date + "\"," +
                "\"state\": \"" + state + "\"" +
                "}";
    }

    public Map<String, Object> toMap() {
        return Map.of(
                "author", author,
                "content", content,
                "date", date,
                "state", state
        );
    }
}