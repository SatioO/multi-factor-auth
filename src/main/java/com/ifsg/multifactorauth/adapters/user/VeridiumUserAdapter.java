package com.ifsg.multifactorauth.adapters.user;


import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.AssignTokenBodyDTO;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.interfaces.IUserAdapter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VeridiumUserAdapter implements IUserAdapter {
    @Override
    public Boolean createUser(CreateUserBodyDTO body) {
        return null;
    }

    @Override
    public Boolean assignToken(AssignTokenBodyDTO data) {
        return null;
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return null;
    }
}
