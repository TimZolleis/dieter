package dev.elektronisch.dieter.common.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class LoginRequest {
    private String username;
    private String password;
}
