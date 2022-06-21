package com.ifsg.multifactorauth.repositories;

import com.ifsg.multifactorauth.entities.UserSoftTokenEntity;
import com.ifsg.multifactorauth.models.enums.TokenStatus;
import com.ifsg.multifactorauth.models.enums.TokenType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SoftTokenRepository extends JpaRepository<UserSoftTokenEntity, UUID> {
    @Modifying
    @Query("UPDATE USER_SOFT_TOKEN t SET t.status = ?2 WHERE t.externalId = ?1 AND t.type = ?3")
    void updateStatusByExternalIdAndType(String externalId, TokenStatus status, TokenType type);

    @Query("SELECT t from USER_SOFT_TOKEN t WHERE t.externalId = ?1 AND t.type = ?2 AND t.status = 0")
    List<UserSoftTokenEntity> findByExternalIdAndType(Pageable pageable, String externalId, TokenType type);
}
