package org.gycoding.accounts.infrastructure.external.auth;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.CreatedUser;
import com.auth0.json.auth.TokenHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AuthFacade {
    TokenHolder login(String email, String password) throws Auth0Exception;
    CreatedUser signUp(String email, String username, String password) throws Auth0Exception;

    String googleAuth();
    TokenHolder handleGoogleResponse(String code) throws Auth0Exception;

    Map<String, Object> getMetadata(String userId) throws Auth0Exception;
    void updateMetadata(String userId, Map<String, Object> metadata) throws Auth0Exception;

    String decode(String jwt);
}
