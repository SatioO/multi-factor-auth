package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.adapters.otp.OTPGeneratorAdapter;
import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.exceptions.BusinessLogicException;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.ChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuth;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class EmailOTPAdapter implements MultiFactorAuth {
    @Autowired
    private OTPGeneratorAdapter otpGeneratorAdapter;

    @Value("${multi_factor.modes.email_otp.code_length}")
    private Integer codeLength;

    @Value("${multi_factor.modes.email_otp.code_expiry}")
    private Integer codeExpiry;

    @Override
    public ChallengeDTO createSession(InitializeChallengeDTO data) {
        try {
            String code = otpGeneratorAdapter.generateOTP(codeLength);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MILLISECOND, codeExpiry);

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
    public Boolean validateSession(MultiFactorEntity entity, VerifyChallengeDTO body) {
        return otpGeneratorAdapter.verifyOTP(body.getCode());
    }
}
