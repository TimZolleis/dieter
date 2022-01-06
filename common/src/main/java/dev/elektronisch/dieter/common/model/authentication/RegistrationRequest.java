package dev.elektronisch.dieter.common.model.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public final class RegistrationRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
