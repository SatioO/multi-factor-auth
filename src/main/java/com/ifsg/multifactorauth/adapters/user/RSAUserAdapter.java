package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.interfaces.UserAdapter;
import com.ifsg.multifactorauth.rest.rsa.RSARestClient;
import com.ifsg.multifactorauth.rest.rsa.models.AuthenticationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RSAUserAdapter implements UserAdapter {
    private final RSARestClient authRestClient;

    public RSAUserAdapter(RSARestClient authRestClient) {
        this.authRestClient = authRestClient;
    }

    @Override
    public UserEntity createUser(CreateUserBodyDTO body) {
        ResponseEntity<AuthenticationResult> response = authRestClient.getAuthToken();
        System.out.println(response.getBody().getAuthenticationToken());
        return null;
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return null;
    }
}