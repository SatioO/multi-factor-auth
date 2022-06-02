package com.ifsg.multifactorauth.repositories;

import com.ifsg.multifactorauth.entities.AuthSessionEntity;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthSessionRepository extends JpaRepository<AuthSessionEntity, UUID> {
    @Query("SELECT u FROM MULTI_FACTOR_AUTH_SESSION u WHERE u.sessionId = ?1 AND u.status = ?2")
    Optional<AuthSessionEntity> findSessionById(UUID sessionId, AuthStatus status);
}
