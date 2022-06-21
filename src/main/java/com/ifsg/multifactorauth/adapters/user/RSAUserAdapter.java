package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.exceptions.BusinessLogicException;
import com.ifsg.multifactorauth.exceptions.InvalidInputException;
import com.ifsg.multifactorauth.models.dtos.AssignTokenBodyDTO;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.interfaces.IUserAdapter;
import com.ifsg.multifactorauth.rest.rsa.RSARestClient;
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
                .map(data -> Optional.ofNullable(data.getBody())
                        .map(d -> {
                            if(!d.isResult()) throw new InvalidInputException(d.getErrorMessage());
                            return d.isResult();
                        })
                        .orElseThrow(() -> new BusinessLogicException("Something went wrong.")))
                .orElseThrow(() -> new BusinessLogicException("Something went wrong."));
    }

    @Override
    public Boolean assignToken(AssignTokenBodyDTO data) {
        if(data.getDeviceType() == null) {
            throw new InvalidInputException("Device type is required");
        }

        return Optional.ofNullable(authRestClient.getAuthToken().getBody())
                .map(r -> this.authRestClient.assignToken(r.getAuthenticationToken(), data.getExternalId()))
                .map(r -> Optional.ofNullable(r.getBody())
                        .map(d -> {
                            if(!d.isResult()) throw new InvalidInputException(d.getErrorMessage());
                            return d.isResult();
                        })
                        .orElseThrow(() -> new BusinessLogicException("Something went wrong.")))
                .orElseThrow(() -> new BusinessLogicException("Something went wrong."));
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return null;
    }
}