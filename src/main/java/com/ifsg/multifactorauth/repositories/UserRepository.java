package com.ifsg.multifactorauth.repositories;

import com.ifsg.multifactorauth.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("SELECT u from MULTI_FACTOR_USERS u WHERE u.externalId = ?1")
    Optional<UserEntity> findByExternalId(String userId);
}
