package dev.elektronisch.dieter.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class AuthenticationRequest {
    private String username;
    private String password;
}
