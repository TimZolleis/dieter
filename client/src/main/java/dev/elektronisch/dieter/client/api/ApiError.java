package dev.elektronisch.dieter.client.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public final class ApiError {
    private String code;
    private String message;

    public ApiError(final Throwable cause) {
        this.code = "CLIENT_EXCEPTION";
        this.message = cause.toString();
    }
}
