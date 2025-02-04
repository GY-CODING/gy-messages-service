package org.gycoding.messages.domain.model.group;

import lombok.Builder;

@Builder
public record MemberMO(
        String userId,
        Boolean isAdmin
) { }