package org.gycoding.accounts.domain.entities;

import lombok.Builder;
import org.gycoding.accounts.domain.enums.MessageStates;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Map;

@Builder
public record Message(
        String author,
        String content,
        String date,
        MessageStates state
) {
    public final static DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .toFormatter();

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