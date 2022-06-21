package com.ifsg.multifactorauth.models.interfaces;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.AssignTokenBodyDTO;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;

import java.util.Optional;

public interface IUserAdapter {
    Boolean createUser(CreateUserBodyDTO data);

    Boolean assignToken(AssignTokenBodyDTO data);

    Optional<UserEntity> getUserDetails(String externalId);
}
