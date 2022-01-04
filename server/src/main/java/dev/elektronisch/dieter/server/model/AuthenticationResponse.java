package dev.elektronisch.dieter.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class AuthenticationResponse {
    private String token;
    private long validUntil;
}
