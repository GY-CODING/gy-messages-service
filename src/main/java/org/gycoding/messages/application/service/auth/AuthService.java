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
                    ChatAPIError.SERVER_ERROR.getCode(),
                    ChatAPIError.SERVER_ERROR.getMessage(),
                    ChatAPIError.SERVER_ERROR.getStatus()
            );
        }
    }
}
