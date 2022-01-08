package dev.elektronisch.dieter.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("\"Hello world, " + authentication.getPrincipal() + "!\"");
    }
}
