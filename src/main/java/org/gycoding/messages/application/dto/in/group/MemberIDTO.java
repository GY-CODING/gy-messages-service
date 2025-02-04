package org.gycoding.messages.application.dto.in.group;

import lombok.Builder;

@Builder
public record MemberIDTO(
        String userId,
        Boolean isAdmin
) { }