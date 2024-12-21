package org.gycoding.messages.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChatAPIError {
    FORBIDDEN("The user has no permission to access this resource.", HttpStatus.FORBIDDEN),
    RESOURCE_NOT_FOUND("This resource was not found.", HttpStatus.NOT_FOUND),
    CONFLICT("An internal conflict between external data and the API has occured. Requested data may already exist.", HttpStatus.CONFLICT),
    JSON_COULD_NOT_BE_PARSED("The JSON could not be parsed.", HttpStatus.CONFLICT),
    DB_ERROR("An error with the database has occurred, sorry for the inconvenience.", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVER_ERROR("An internal server error has occurred, sorry for the inconvenience.", HttpStatus.INTERNAL_SERVER_ERROR);

    public final String code;
    public final String message;
    public final HttpStatus status;

    private ChatAPIError(String message, HttpStatus status) {
        this.code       = this.name();
        this.message    = message;
        this.status     = status;
    }
}