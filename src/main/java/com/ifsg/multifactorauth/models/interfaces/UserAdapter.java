package com.ifsg.multifactorauth.models.interfaces;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;

import java.util.Optional;

public interface UserAdapter {
    UserEntity createUser(CreateUserBodyDTO body);
    Optional<UserEntity> getUserDetails(String externalId);
}
