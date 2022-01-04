package dev.elektronisch.dieter.server.controller;

import dev.elektronisch.dieter.server.model.AuthenticationRequest;
import dev.elektronisch.dieter.server.model.AuthenticationResponse;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/authentication")
public final class AuthenticationController {

    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        throw new NotImplementedException();
    }
}
