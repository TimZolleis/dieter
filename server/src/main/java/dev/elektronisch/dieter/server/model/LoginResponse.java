package dev.elektronisch.dieter.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class LoginResponse {
    private String token;
    private long expirationDate;
}
