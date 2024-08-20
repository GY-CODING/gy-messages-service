package org.gycoding.messages.application.service.auth;

import org.gycoding.messages.domain.exceptions.ChatAPIError;
import org.gycoding.messages.infrastructure.external.auth.AuthFacade;
import org.gycoding.exceptions.model.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthRepository {
    @Autowired
    private AuthFacade authFacade = null;

    @Override
    public String decode(String token) throws APIException {
        try {
            return authFacade.decode(token);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.INVALID_AUTH.getCode(),
                    ChatAPIError.INVALID_AUTH.getMessage(),
                    ChatAPIError.INVALID_AUTH.getStatus()
            );
        }
    }
}
