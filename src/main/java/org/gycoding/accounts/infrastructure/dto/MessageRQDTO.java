package org.gycoding.accounts.infrastructure.dto;

import lombok.Builder;

@Builder
public record MessageRQDTO(
    String message
) { }