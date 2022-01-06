package dev.elektronisch.dieter.server.exception;

import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseErrorCode("ALREADY_AUTHENTICATED")
public final class AlreadyAuthenticatedException extends RuntimeException {
}