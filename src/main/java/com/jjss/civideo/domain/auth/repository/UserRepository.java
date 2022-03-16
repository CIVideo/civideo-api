package com.jjss.civideo.domain.auth.repository;

import com.jjss.civideo.domain.auth.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
