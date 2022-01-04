package dev.elektronisch.dieter.server.repository;

import dev.elektronisch.dieter.server.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
}
