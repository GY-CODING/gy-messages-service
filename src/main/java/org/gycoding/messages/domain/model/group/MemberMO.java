package org.gycoding.messages.domain.model.group;

import lombok.Builder;

@Builder
public record MemberMO(
        String userId,
        String username,
        Boolean isAdmin
) { }