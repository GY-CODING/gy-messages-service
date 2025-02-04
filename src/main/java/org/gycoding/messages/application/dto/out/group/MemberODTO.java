package org.gycoding.messages.application.dto.out.group;

import lombok.Builder;

@Builder
public record MemberODTO(
        String userId,
        Boolean isAdmin
) { }