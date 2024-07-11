package org.gycoding.accounts.application.service.auth;

import org.gycoding.accounts.domain.entities.EntityChat;
import org.gycoding.accounts.domain.entities.Message;
import org.gycoding.accounts.domain.exceptions.ChatAPIException;
import org.gycoding.accounts.infrastructure.dto.ChatRQDTO;

import java.util.List;

public interface AuthRepository {
    Boolean isAdmin(String userId) throws ChatAPIException;
    String decode(String jwt) throws ChatAPIException;
}
