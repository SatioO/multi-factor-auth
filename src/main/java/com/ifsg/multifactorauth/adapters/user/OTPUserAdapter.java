package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.adapters.otp.OTPGeneratorAdapter;
import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.exceptions.ResourceNotFoundException;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.enums.UserStatus;
import com.ifsg.multifactorauth.models.interfaces.IUserAdapter;
import com.ifsg.multifactorauth.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OTPUserAdapter implements IUserAdapter {
    private final UserRepository userRepository;
    private final OTPGeneratorAdapter otpGeneratorAdapter;

    public OTPUserAdapter(UserRepository userRepository, OTPGeneratorAdapter otpGeneratorAdapter) {
        this.userRepository = userRepository;
        this.otpGeneratorAdapter = otpGeneratorAdapter;
    }

    @Override
    public Boolean createUser(CreateUserBodyDTO data) {
       userRepository.save(
                UserEntity.builder()
                        .externalId(data.getExternalId())
                        .firstName(data.getFirstName())
                        .lastName(data.getLastName())
                        .username(data.getPrimaryEmail())
                        .primaryEmail(data.getPrimaryEmail())
                        .secondaryMail(data.getSecondaryEmail())
                        .phoneCountryCode(data.getPhoneCountryCode())
                        .phoneNumber(data.getPhoneNumber())
                        .status(UserStatus.ACTIVE)
                        .createdTime(new Date())
                        .updatedTime(new Date())
                        .build());
        return true;
    }

    @Override
    public Boolean assignToken(String externalId) {
        UserEntity foundUser = userRepository
                .findByExternalId(externalId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        userRepository.save(
                foundUser.toBuilder()
                        .emailToken(otpGeneratorAdapter.generateSecret(32))
                        .smsToken(otpGeneratorAdapter.generateSecret(32)).build()
        );

        return true;
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return userRepository.findByExternalId(externalId);
    }
}
