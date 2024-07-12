package org.gycoding.accounts.infrastructure.dto;

import lombok.Builder;

@Builder
public record GYAccountsChatDTO(
    String chatId,
    boolean admin
) {
    @Override
    public String toString() {
        return "{" +
                "\"chatId\": \"" + chatId + "\"" +
                ", \"admin\": " + admin +
                "}";
    }
}
