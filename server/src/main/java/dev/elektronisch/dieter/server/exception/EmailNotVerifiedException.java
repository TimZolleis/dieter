package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("EMAIL_NOT_VERIFIED")
public final class EmailNotVerifiedException extends RuntimeException {
}
