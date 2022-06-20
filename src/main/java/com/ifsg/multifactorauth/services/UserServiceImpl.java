package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.adapters.user.UserAdapter;
import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.exceptions.ResourceNotFoundException;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.enums.AuthMethod;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserAdapter userAdapter;

    public UserServiceImpl(UserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    @Override
    public void createUser(AuthMethod method, CreateUserBodyDTO body) {
        userAdapter.getAdapter(method).createUser(body);
    }

    @Override
    public UserEntity getUserDetails(AuthMethod method, String externalId) {
        return userAdapter
                .getAdapter(method)
                .getUserDetails(externalId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    @Override
    public void assignTokenToUser(AuthMethod method, String externalId) {
        userAdapter.getAdapter(method).assignToken(externalId);
    }
}
