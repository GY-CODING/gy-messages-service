package org.gycoding.messages.infrastructure.external.auth;

import org.springframework.stereotype.Repository;

@Repository
public interface AuthFacade {
    String decode(String token);
}
