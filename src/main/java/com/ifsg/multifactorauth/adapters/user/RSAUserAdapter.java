package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.exceptions.BusinessLogicException;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.interfaces.IUserAdapter;
import com.ifsg.multifactorauth.rest.rsa.RSARestClient;
import com.ifsg.multifactorauth.rest.rsa.models.ServiceResult;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RSAUserAdapter implements IUserAdapter {
    private final RSARestClient authRestClient;

    public RSAUserAdapter(RSARestClient authRestClient) {
        this.authRestClient = authRestClient;
    }

    @Override
    public Boolean createUser(CreateUserBodyDTO body) {
        return Optional.ofNullable(authRestClient.getAuthToken().getBody())
                .map(data -> this.authRestClient.createUser(data.getAuthenticationToken(), body))
                .map(data -> Optional.ofNullable(data.getBody()).map(ServiceResult::isResult).orElse(false))
                .orElseThrow(() -> new BusinessLogicException("Something went wrong..!!!"));
    }

    @Override
    public Boolean assignToken(String externalId) {
        return null;
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return null;
    }
}