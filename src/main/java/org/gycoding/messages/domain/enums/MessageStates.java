package org.gycoding.messages.domain.enums;

public enum MessageStates {
    PREPARED("PREPARED"),
    SENT("SENT"),
    RECEIVED("RECEIVED"),
    READ("READ");

    public final String state;

    private MessageStates(String state) {
        this.state = state;
    }
}
