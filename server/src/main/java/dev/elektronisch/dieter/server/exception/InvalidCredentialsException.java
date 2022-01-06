package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("INVALID_CREDENTIALS")
public final class InvalidCredentialsException extends RuntimeException {
}
