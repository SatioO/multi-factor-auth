package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.adapters.otp.IOTPGeneratorAdapter;
import com.ifsg.multifactorauth.config.OTPPolicyConfig;
import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.entities.UserSoftTokenEntity;
import com.ifsg.multifactorauth.exceptions.BusinessLogicException;
import com.ifsg.multifactorauth.exceptions.ResourceNotFoundException;
import com.ifsg.multifactorauth.models.dtos.CreateChallengeAdapterDTO;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeBodyDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeAdapterDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeBodyDTO;
import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.enums.TokenType;
import com.ifsg.multifactorauth.models.interfaces.IMultiFactorAuthAdapter;
import com.ifsg.multifactorauth.repositories.SoftTokenRepository;
import com.ifsg.multifactorauth.repositories.UserRepository;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class SMSOTPAdapter implements IMultiFactorAuthAdapter {
    private final IOTPGeneratorAdapter IOTPGeneratorAdapter;
    private final OTPPolicyConfig otpPolicyConfig;
    private final UserRepository userRepository;
    private final SoftTokenRepository softTokenRepository;

    public SMSOTPAdapter(OTPPolicyConfig otpPolicyConfig, IOTPGeneratorAdapter IOTPGeneratorAdapter, SoftTokenRepository softTokenRepository, UserRepository userRepository) {
        this.IOTPGeneratorAdapter = IOTPGeneratorAdapter;
        this.otpPolicyConfig = otpPolicyConfig;
        this.userRepository = userRepository;
        this.softTokenRepository = softTokenRepository;
    }

    @Override
    public CreateChallengeAdapterDTO createSession(InitializeChallengeBodyDTO data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSoftTokenEntity token = softTokenRepository
                .findByExternalIdAndType(PageRequest.of(0, 1), authentication.getName(), TokenType.SMS)
                .stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No Token assigned to user."));

        try {
            IOTPGeneratorAdapter.generateOTP(token.getToken());

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, otpPolicyConfig.getCodeExpiry() + otpPolicyConfig.getCodeExpiry() / 2);

            return CreateChallengeAdapterDTO.builder()
                    .authMethod(data.getAuthMethod())
                    .status(AuthStatus.CHALLENGE)
                    .expiryTime(calendar.getTime())
                    .createdTime(new Date())
                    .build();
        } catch (CodeGenerationException e) {
            throw new BusinessLogicException(e.getMessage());
        }
    }

    @Override
    public VerifyChallengeAdapterDTO verifyChallenge(MultiFactorEntity entity, VerifyChallengeBodyDTO body) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSoftTokenEntity token = softTokenRepository
                .findByExternalIdAndType(PageRequest.of(0, 1), authentication.getName(), TokenType.EMAIL)
                .stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No Token assigned to user."));

        boolean isValidCode = IOTPGeneratorAdapter.verifyOTP(token.getToken(), body.getAnswer());

        AuthStatus authStatus = isValidCode ? AuthStatus.SUCCESS : AuthStatus.FAIL;
        AuthReasonCode authReasonCode = isValidCode ? AuthReasonCode.CHALLENGE_VERIFIED : AuthReasonCode.CHALLENGE_FAILED;

        return VerifyChallengeAdapterDTO.builder()
                .authStatus(authStatus)
                .authReasonCode(authReasonCode)
                .build();
    }
}
