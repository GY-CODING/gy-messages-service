package org.gycoding.accounts.infrastructure.external.gyaccounts;

import org.gycoding.accounts.infrastructure.dto.GYAccountsChatDTO;
import org.gycoding.exceptions.model.APIException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GYAccountsFacade {
    void addChat(String jwt, String chatId, Boolean isAdmin);
    void removeChat(String jwt, UUID chatId);
    List<GYAccountsChatDTO> listChats(String jwt) throws APIException;
}
