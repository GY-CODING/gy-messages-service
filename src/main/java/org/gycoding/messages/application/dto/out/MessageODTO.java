package org.gycoding.messages.application.dto.out;

import lombok.Builder;
import org.gycoding.messages.application.dto.out.group.MemberODTO;
import org.gycoding.messages.shared.MessageStates;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Builder
public record MessageODTO(
        MemberODTO author,
        String content,
        String date,
        MessageStates state
) {
    public final static DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .toFormatter();
}