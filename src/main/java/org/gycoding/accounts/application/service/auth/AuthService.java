package org.gycoding.accounts.application.service.auth;

import org.gycoding.accounts.domain.exceptions.ChatAPIError;
import org.gycoding.accounts.infrastructure.external.auth.AuthFacade;
import org.gycoding.exceptions.model.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthRepository {
    @Autowired
    private AuthFacade authFacade = null;

    @Override
    public String decode(String jwt) throws APIException {
        try {
            return authFacade.decode(jwt);
        } catch(Exception e) {
            throw new APIException(
                    ChatAPIError.INVALID_AUTH.getCode(),
                    ChatAPIError.INVALID_AUTH.getMessage(),
                    ChatAPIError.INVALID_AUTH.getStatus()
            );
        }
    }
}
