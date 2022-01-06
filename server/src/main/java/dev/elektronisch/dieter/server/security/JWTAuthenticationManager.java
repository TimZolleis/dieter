package dev.elektronisch.dieter.server.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public final class JWTAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /*
          JWT-Authentication is stateless, therefore the purpose of this class is only to calm Spring Security!
          Due to the fact that the JWTAuthenticationTokenFilter is already verifying the token's validity,
          there's no need for the JWTAuthenticationManager to redo that, we return the original verified token.
         */
        return authentication;
    }
}
