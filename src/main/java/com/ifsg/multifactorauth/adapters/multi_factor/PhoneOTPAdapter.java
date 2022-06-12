package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.adapters.otp.OTPGeneratorAdapter;
import com.ifsg.multifactorauth.config.OTPPolicyConfig;
import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.entities.UserEntity;
import com.ifsg.multifactorauth.exceptions.BusinessLogicException;
import com.ifsg.multifactorauth.exceptions.ResourceNotFoundException;
import com.ifsg.multifactorauth.models.dtos.CreateChallengeAdapterDTO;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeBodyDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeAdapterDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeBodyDTO;
import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuthAdapter;
import com.ifsg.multifactorauth.repositories.UserRepository;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class PhoneOTPAdapter implements MultiFactorAuthAdapter {
    private final OTPGeneratorAdapter otpGeneratorAdapter;
    private final OTPPolicyConfig otpPolicyConfig;
    private final UserRepository userRepository;

    public PhoneOTPAdapter(OTPPolicyConfig otpPolicyConfig, OTPGeneratorAdapter otpGeneratorAdapter, UserRepository userRepository) {
        this.otpGeneratorAdapter = otpGeneratorAdapter;
        this.otpPolicyConfig = otpPolicyConfig;
        this.userRepository = userRepository;
    }

    @Override
    public CreateChallengeAdapterDTO createSession(InitializeChallengeBodyDTO data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = this.userRepository.findByExternalId(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        try {
            String code = otpGeneratorAdapter.generateOTP(user.getSmsToken());
            System.out.println(user.getSmsToken());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, otpPolicyConfig.getCodeExpiry() + otpPolicyConfig.getCodeExpiry() / 2);

            return CreateChallengeAdapterDTO.builder()
                    .code(code)
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
        boolean isValidCode = otpGeneratorAdapter.verifyOTP(body.getAnswer());

        AuthStatus authStatus = isValidCode ? AuthStatus.SUCCESS : AuthStatus.FAIL;
        AuthReasonCode authReasonCode = isValidCode ? AuthReasonCode.CHALLENGE_VERIFIED : AuthReasonCode.CHALLENGE_FAILED;

        return VerifyChallengeAdapterDTO.builder()
                .authStatus(authStatus)
                .authReasonCode(authReasonCode)
                .build();
    }
}
