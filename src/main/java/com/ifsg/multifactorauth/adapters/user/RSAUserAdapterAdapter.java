package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.interfaces.UserAdapter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RSAUserAdapterAdapter implements UserAdapter {
    @Override
    public UserEntity createUser(CreateUserBodyDTO body) {
        return null;
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return null;
    }
}