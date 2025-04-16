package org.gycoding.messages.domain.model;

import lombok.Builder;
import org.gycoding.messages.domain.model.group.MemberMO;
import org.gycoding.messages.shared.MessageStates;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Builder
public record MessageMO(
        MemberMO author,
        String content,
        String date,
        MessageStates state
) {
    public final static DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .toFormatter();
}