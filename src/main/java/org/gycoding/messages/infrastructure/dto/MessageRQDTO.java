package org.gycoding.messages.infrastructure.dto;

import lombok.Builder;

@Builder
public record MessageRQDTO(
    String message
) { }
