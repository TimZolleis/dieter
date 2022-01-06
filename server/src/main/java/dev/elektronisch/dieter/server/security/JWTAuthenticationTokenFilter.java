package dev.elektronisch.dieter.server.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import dev.elektronisch.dieter.server.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public final class JWTAuthenticationTokenFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final String headerName;

    public JWTAuthenticationTokenFilter(AuthenticationService authenticationService,
                                        @Value("${jwt.header}") String headerName) {
        this.authenticationService = authenticationService;
        this.headerName = headerName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {
        String tokenHeader = request.getHeader(headerName);

        // If bearer token is present
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String authToken = tokenHeader.substring(7);

            // Verify token
            final DecodedJWT verifiedToken = authenticationService.verifyToken(authToken);
            if (verifiedToken != null) {
                SecurityContextHolder.getContext().setAuthentication(new JWTAuthentication(verifiedToken));
            }
        }

        chain.doFilter(request, response);
    }
}