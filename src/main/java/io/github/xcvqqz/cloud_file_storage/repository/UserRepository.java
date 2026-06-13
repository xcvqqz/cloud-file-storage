package io.github.xcvqqz.cloud_file_storage.repository;

import io.github.xcvqqz.cloud_file_storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
}