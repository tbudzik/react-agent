package com.example.crud.repository;

import com.example.crud.model.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Long> {
    Optional<FieldEntity> findByUuid(String uuid);
}
