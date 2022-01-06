package dev.elektronisch.dieter.server.controller;

import dev.elektronisch.dieter.server.exception.AccountNotFoundException;
import dev.elektronisch.dieter.server.exception.AlreadyAuthenticatedException;
import dev.elektronisch.dieter.server.model.LoginRequest;
import dev.elektronisch.dieter.server.model.LoginResponse;
import dev.elektronisch.dieter.server.security.JWTAuthentication;
import dev.elektronisch.dieter.server.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JWTAuthentication) {
            throw new AlreadyAuthenticatedException();
        }

        return new ResponseEntity<>(service.handleLogin(request), HttpStatus.OK);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyAuthenticatedException.class)
    public ResponseEntity<String> handleAlreadyAuthenticatedException(AlreadyAuthenticatedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
