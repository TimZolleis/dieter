package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("USERNAME_TAKEN")
public final class UsernameTakenException extends RuntimeException {
}
