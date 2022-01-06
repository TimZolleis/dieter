package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("EMAIL_TAKEN")
public final class EmailTakenException extends RuntimeException {
}
