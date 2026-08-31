package io.github.xcvqqz.cloud_file_storage.repository;

import io.github.xcvqqz.cloud_file_storage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<User, Long> {
}
