package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.interfaces.UserAdapter;
import com.ifsg.multifactorauth.rest.rsa.RSARestClient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RSAUserAdapterAdapter implements UserAdapter {
    private final RSARestClient authRestClient;

    public RSAUserAdapterAdapter(RSARestClient authRestClient) {
        this.authRestClient = authRestClient;
    }

    @Override
    public UserEntity createUser(CreateUserBodyDTO body) {
        authRestClient.getAuthToken();
        return null;
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return null;
    }
}