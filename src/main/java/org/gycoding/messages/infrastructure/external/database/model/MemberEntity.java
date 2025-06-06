package org.gycoding.messages.infrastructure.external.database.model;

import lombok.Builder;

@Builder
public record MemberEntity(
        String userId,
        String username,
        Boolean isAdmin
) { }