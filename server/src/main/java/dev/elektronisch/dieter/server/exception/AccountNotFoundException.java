package dev.elektronisch.dieter.server.exception;

public final class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
