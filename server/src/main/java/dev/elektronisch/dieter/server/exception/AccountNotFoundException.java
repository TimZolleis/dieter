package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;

@ResponseErrorCode("ACCOUNT_NOT_FOUND")
public final class AccountNotFoundException extends RuntimeException {
}
