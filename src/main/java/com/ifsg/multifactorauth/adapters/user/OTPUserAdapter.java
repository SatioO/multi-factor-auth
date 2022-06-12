package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.adapters.otp.OTPGeneratorAdapter;
import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.enums.UserStatus;
import com.ifsg.multifactorauth.models.interfaces.UserAdapter;
import com.ifsg.multifactorauth.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OTPUserAdapter implements UserAdapter {
    private final UserRepository userRepository;
    private final OTPGeneratorAdapter otpGeneratorAdapter;

    public OTPUserAdapter(UserRepository userRepository, OTPGeneratorAdapter otpGeneratorAdapter) {
        this.userRepository = userRepository;
        this.otpGeneratorAdapter = otpGeneratorAdapter;
    }

    @Override
    public UserEntity createUser(CreateUserBodyDTO data) {
        return userRepository.save(
                UserEntity.builder()
                        .externalId(data.getExternalId())
                        .firstName(data.getFirstName())
                        .lastName(data.getLastName())
                        .username(data.getPrimaryEmail())
                        .primaryEmail(data.getPrimaryEmail())
                        .secondaryMail(data.getSecondaryEmail())
                        .phoneCountryCode(data.getPhoneCountryCode())
                        .phoneNumber(data.getPhoneNumber())
                        .emailToken(otpGeneratorAdapter.generateSecret(32))
                        .smsToken(otpGeneratorAdapter.generateSecret(32))
                        .status(UserStatus.ACTIVE)
                        .createdTime(new Date())
                        .updatedTime(new Date())
                        .build());
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return userRepository.findByExternalId(externalId);
    }
}
