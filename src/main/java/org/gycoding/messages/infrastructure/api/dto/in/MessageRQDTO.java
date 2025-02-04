package org.gycoding.messages.infrastructure.api.dto.in;

import lombok.Builder;

@Builder
public record MessageRQDTO(
    String message
) { }
