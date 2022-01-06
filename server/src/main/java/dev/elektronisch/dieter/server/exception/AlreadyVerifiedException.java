package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("ALREADY_VERIFIED")
public final class AlreadyVerifiedException extends RuntimeException {
}
