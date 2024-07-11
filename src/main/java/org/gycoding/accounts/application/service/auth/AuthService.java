package org.gycoding.accounts.application.service.auth;

import org.gycoding.accounts.domain.entities.EntityChat;
import org.gycoding.accounts.domain.entities.Message;
import org.gycoding.accounts.domain.enums.MessageStates;
import org.gycoding.accounts.domain.enums.ServerStatus;
import org.gycoding.accounts.domain.exceptions.ChatAPIException;
import org.gycoding.accounts.infrastructure.dto.ChatRQDTO;
import org.gycoding.accounts.infrastructure.external.auth.AuthFacade;
import org.gycoding.accounts.infrastructure.external.database.service.ChatMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements AuthRepository {
    @Autowired
    private AuthFacade authFacade = null;

    @Override
    public Boolean isAdmin(String userId) throws ChatAPIException {
        try {
            authFacade.getMetadata(userId);

            return false;
        } catch(Exception e) {
            throw new ChatAPIException(ServerStatus.METADATA_NOT_FOUND);
        }
    }

    @Override
    public String decode(String jwt) throws ChatAPIException {
        try {
            return authFacade.decode(jwt);
        } catch(Exception e) {
            throw new ChatAPIException(ServerStatus.INVALID_AUTH);
        }
    }
}
