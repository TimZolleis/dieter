package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("ALREADY_AUTHENTICATED")
public final class AlreadyAuthenticatedException extends RuntimeException {
}