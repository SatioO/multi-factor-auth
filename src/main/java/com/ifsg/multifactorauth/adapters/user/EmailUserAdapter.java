package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.adapters.otp.IOTPGeneratorAdapter;
import com.ifsg.multifactorauth.config.OTPPolicyConfig;
import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.entities.UserSoftTokenEntity;
import com.ifsg.multifactorauth.exceptions.InvalidInputException;
import com.ifsg.multifactorauth.exceptions.ResourceNotFoundException;
import com.ifsg.multifactorauth.models.dtos.AssignTokenBodyDTO;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.enums.TokenStatus;
import com.ifsg.multifactorauth.models.enums.TokenType;
import com.ifsg.multifactorauth.models.enums.UserStatus;
import com.ifsg.multifactorauth.models.interfaces.IUserAdapter;
import com.ifsg.multifactorauth.repositories.SoftTokenRepository;
import com.ifsg.multifactorauth.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EmailUserAdapter implements IUserAdapter {
    private final UserRepository userRepository;
    private final SoftTokenRepository softTokenRepository;
    private final IOTPGeneratorAdapter IOTPGeneratorAdapter;
    private final OTPPolicyConfig otpPolicyConfig;

    public EmailUserAdapter(UserRepository userRepository, OTPPolicyConfig otpPolicyConfig, SoftTokenRepository softTokenRepository, IOTPGeneratorAdapter IOTPGeneratorAdapter) {
        this.userRepository = userRepository;
        this.IOTPGeneratorAdapter = IOTPGeneratorAdapter;
        this.softTokenRepository = softTokenRepository;
        this.otpPolicyConfig = otpPolicyConfig;
    }

    @Override
    public Boolean createUser(CreateUserBodyDTO data) {
        Optional<UserEntity> user = userRepository.findByExternalId(data.getExternalId());

        if(user.isPresent()) {
            throw new InvalidInputException("Principal with userid already exists in realm: " + data.getExternalId());
        }

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
    public Boolean assignToken(AssignTokenBodyDTO data) {
        return userRepository
                .findByExternalId(data.getExternalId()).map(foundUser -> {
                    softTokenRepository
                            .findByExternalIdAndType(PageRequest.of(0, 1), data.getExternalId(), TokenType.EMAIL)
                            .stream()
                            .findFirst()
                            .ifPresent((token) -> softTokenRepository.save(
                                    token.toBuilder().status(TokenStatus.INACTIVE).build()));


                    softTokenRepository.save(
                            UserSoftTokenEntity.builder()
                                    .externalId(data.getExternalId())
                                    .token(IOTPGeneratorAdapter.generateSecret(otpPolicyConfig.getSoftTokenLength()))
                                    .type(TokenType.EMAIL)
                                    .createdTime(new Date())
                                    .updatedTime(new Date())
                                    .status(TokenStatus.ACTIVE).build()
                    );

                    return true;
                }).orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public Optional<UserEntity> getUserDetails(String externalId) {
        return userRepository.findByExternalId(externalId);
    }
}
