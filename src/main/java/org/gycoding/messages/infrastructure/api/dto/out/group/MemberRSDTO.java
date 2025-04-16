package org.gycoding.messages.infrastructure.api.dto.out.group;

import lombok.Builder;

@Builder
public record MemberRSDTO(
        String userId,
        String username,
        Boolean isAdmin
) { }