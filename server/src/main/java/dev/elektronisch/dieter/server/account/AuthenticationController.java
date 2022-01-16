package dev.elektronisch.dieter.server.account;

import dev.elektronisch.dieter.common.model.authentication.*;
import dev.elektronisch.dieter.server.exception.AlreadyAuthenticatedException;
import dev.elektronisch.dieter.server.security.JWTAuthentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(final AuthenticationService service) {
        this.service = service;
    }

    @GetMapping("/auth")
    public ResponseEntity<Void> auth() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final LoginRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JWTAuthentication) {
            throw new AlreadyAuthenticatedException();
        }

        return new ResponseEntity<>(service.handleLogin(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody final RegistrationRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JWTAuthentication) {
            throw new AlreadyAuthenticatedException();
        }

        final RegistrationResponse response = new RegistrationResponse(service.handleRegistration(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> register(@RequestBody final VerificationRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JWTAuthentication) {
            throw new AlreadyAuthenticatedException();
        }

        service.handleVerification(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
