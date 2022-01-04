package dev.elektronisch.dieter.server.user;

import dev.elektronisch.dieter.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public final class UserService {

    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        // For testing purpose only
        repository.findAll().forEach(System.out::println);
    }
}
