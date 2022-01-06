package dev.elektronisch.dieter.server.exception;

public final class AlreadyAuthenticatedException extends RuntimeException {
    public AlreadyAuthenticatedException() {
        super("Already authorized.");
    }
}