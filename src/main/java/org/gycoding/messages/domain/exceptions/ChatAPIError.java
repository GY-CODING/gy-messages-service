package org.gycoding.messages.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ChatAPIError {
    HOME_NOT_FOUND("API reference not found.", HttpStatus.NOT_FOUND),

    CHAT_NOT_FOUND("Specified chat was not found.", HttpStatus.NOT_FOUND),
    CHAT_EXISTS("Chat already exists.", HttpStatus.CONFLICT),

    MESSAGE_NOT_SENT("Message was not sent.", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_AUTH("Invalid authentication token.", HttpStatus.UNAUTHORIZED),
    METADATA_NOT_FOUND("Metadata not found.", HttpStatus.NOT_FOUND),
    METADATA_CHATS_NOT_FOUND("Chats metadata not found.", HttpStatus.NOT_FOUND),
    USER_NOT_ADMIN("User is not the admin of this chat.", HttpStatus.FORBIDDEN),
    USER_NOT_MEMBER("User is not a member of this chat.", HttpStatus.CONFLICT),
    USER_ALREADY_MEMBER("User is already a member of this chat.", HttpStatus.CONFLICT),

    BAD_REQUEST("The endpoint is malformed.", HttpStatus.BAD_REQUEST),
    JSON_COULD_NOT_BE_PARSED("The JSON could not be parsed.", HttpStatus.CONFLICT),
    DB_ERROR("An error with the database has occurred, sorry for the inconvenience.", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTH_ERROR("An error with the authentication service has occurred, sorry for the inconvenience.", HttpStatus.INTERNAL_SERVER_ERROR),
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