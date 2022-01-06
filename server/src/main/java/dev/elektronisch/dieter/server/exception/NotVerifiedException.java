package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("NOT_VERIFIED")
public final class NotVerifiedException extends RuntimeException {
}
