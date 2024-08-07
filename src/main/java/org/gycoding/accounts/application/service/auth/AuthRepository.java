package org.gycoding.accounts.application.service.auth;

import org.gycoding.exceptions.model.APIException;

public interface AuthRepository {
    String decode(String jwt) throws APIException;
}
