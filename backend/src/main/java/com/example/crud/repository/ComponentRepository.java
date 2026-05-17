package com.example.crud.repository;

import com.example.crud.model.ComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentRepository extends JpaRepository<ComponentEntity, Long> {
    Optional<ComponentEntity> findByUuid(String uuid);
}
