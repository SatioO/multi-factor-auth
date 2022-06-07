package com.ifsg.multifactorauth.repositories;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MultiFactorRepository extends JpaRepository<MultiFactorEntity, UUID> {
    @Query("SELECT u FROM MULTI_FACTOR_AUTH_SESSION u WHERE u.sessionId = ?1")
    Optional<MultiFactorEntity> findBySessionId(UUID sessionId);
}
