package com.ifsg.multifactorauth.repositories;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MultiFactorRepository extends JpaRepository<MultiFactorEntity, UUID> {
}
