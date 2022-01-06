package dev.elektronisch.dieter.common.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class TokenResponse {
    private String token;
    private long expirationDate;
}
