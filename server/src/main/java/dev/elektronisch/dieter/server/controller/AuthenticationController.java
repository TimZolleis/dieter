package dev.elektronisch.dieter.server.controller;

import dev.elektronisch.dieter.server.exception.AlreadyAuthenticatedException;
import dev.elektronisch.dieter.server.model.LoginRequest;
import dev.elektronisch.dieter.server.model.RegistrationRequest;
import dev.elektronisch.dieter.server.model.TokenResponse;
import dev.elektronisch.dieter.server.security.JWTAuthentication;
import dev.elektronisch.dieter.server.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JWTAuthentication) {
            throw new AlreadyAuthenticatedException();
        }

        return new ResponseEntity<>(service.handleLogin(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegistrationRequest request) {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JWTAuthentication) {
            throw new AlreadyAuthenticatedException();
        }

        service.handleRegistration(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
