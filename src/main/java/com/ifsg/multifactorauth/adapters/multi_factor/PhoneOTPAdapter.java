package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.adapters.otp.OTPGeneratorAdapter;
import com.ifsg.multifactorauth.config.OTPPolicyConfig;
import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.exceptions.BusinessLogicException;
import com.ifsg.multifactorauth.models.dtos.ChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuth;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class PhoneOTPAdapter implements MultiFactorAuth {
    private final OTPGeneratorAdapter otpGeneratorAdapter;
    private final OTPPolicyConfig otpPolicyConfig;

    public PhoneOTPAdapter(OTPPolicyConfig otpPolicyConfig, OTPGeneratorAdapter otpGeneratorAdapter) {
        this.otpGeneratorAdapter = otpGeneratorAdapter;
        this.otpPolicyConfig = otpPolicyConfig;
    }

    @Override
    public ChallengeDTO createSession(InitializeChallengeDTO data) {
        try {
            String code = otpGeneratorAdapter.generateOTP();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, otpPolicyConfig.getCodeExpiry() + otpPolicyConfig.getCodeExpiry() / 2);

            return ChallengeDTO.builder()
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
    public Boolean verifyChallenge(MultiFactorEntity entity, VerifyChallengeDTO body) {
        return otpGeneratorAdapter.verifyOTP(body.getAnswer());
    }
}
