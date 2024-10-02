package org.gycoding.messages.infrastructure.external.gyaccounts;

import org.gycoding.messages.infrastructure.dto.GYAccountsChatDTO;
import org.gycoding.exceptions.model.APIException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GYAccountsFacade {
    void addChat(String token, String chatId, Boolean isAdmin);
    void removeChat(String token, UUID chatId);
    List<GYAccountsChatDTO> listChats(String token) throws Exception;
}
