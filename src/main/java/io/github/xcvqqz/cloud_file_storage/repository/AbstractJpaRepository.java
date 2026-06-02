package io.github.xcvqqz.cloud_file_storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbstractJpaRepository<T,K> extends JpaRepository<T, K> {
}
