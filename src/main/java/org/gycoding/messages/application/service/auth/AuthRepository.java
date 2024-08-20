package org.gycoding.messages.application.service.auth;

import org.gycoding.exceptions.model.APIException;

public interface AuthRepository {
    String decode(String token) throws APIException;
}
