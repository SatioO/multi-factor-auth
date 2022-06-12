package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.enums.AuthMethod;

public interface UserService {
    UserEntity createUser(AuthMethod authMethod, CreateUserBodyDTO body);
    UserEntity getUserDetails(AuthMethod authMethod, String externalId);
}
