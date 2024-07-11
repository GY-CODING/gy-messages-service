package org.gycoding.accounts.infrastructure.external.auth;

import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.CreatedUser;
import com.auth0.json.auth.TokenHolder;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface AuthFacade {
    Map<String, Object> getMetadata(String userId) throws Auth0Exception;
    void updateMetadata(String userId, Map<String, Object> metadata) throws Auth0Exception;

    String decode(String jwt);
}
