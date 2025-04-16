package org.gycoding.messages.domain.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GYAccountsFacade {
    List<UUID> listChats(String userId);
    void addChat(String userId, UUID chatId, Boolean isAdmin);
    void removeChat(String userId, UUID chatId);
    String getUsername(String userId);
}
