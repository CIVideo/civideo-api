package com.jjss.civideo.domain.user.repository;

import com.jjss.civideo.domain.user.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByProviderId(String id);

}