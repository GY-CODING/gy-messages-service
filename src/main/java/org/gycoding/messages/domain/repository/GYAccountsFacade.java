package org.gycoding.messages.domain.repository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GYAccountsFacade {
    void addChat(String userId, UUID chatId, Boolean isAdmin);
    void removeChat(String userId, UUID chatId);
}
