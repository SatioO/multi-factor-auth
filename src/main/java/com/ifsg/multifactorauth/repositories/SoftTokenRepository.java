package com.ifsg.multifactorauth.repositories;

import com.ifsg.multifactorauth.entities.UserSoftTokenEntity;
import com.ifsg.multifactorauth.models.enums.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SoftTokenRepository extends JpaRepository<UserSoftTokenEntity, UUID> {
    @Modifying
    @Query("UPDATE USER_SOFT_TOKEN t SET t.status = ?1 WHERE t.externalId = ?2")
    void updateTokenStatusByExternalId(TokenStatus status, String externalId);
}
