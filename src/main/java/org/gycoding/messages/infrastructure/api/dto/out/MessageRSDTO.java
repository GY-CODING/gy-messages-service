package org.gycoding.messages.infrastructure.api.dto.out;

import lombok.Builder;
import org.gycoding.messages.shared.MessageStates;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Builder
public record MessageRSDTO(
        String author,
        String content,
        String date,
        MessageStates state
) {
    public final static DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .toFormatter();
}