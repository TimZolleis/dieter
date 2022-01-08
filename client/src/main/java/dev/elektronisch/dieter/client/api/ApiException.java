package dev.elektronisch.dieter.client.api;

import lombok.Getter;

@Getter
public final class ApiException extends RuntimeException {
    private final ApiError error;

    public ApiException(final ApiError error) {
        super(error.toString());
        this.error = error;
    }
}
