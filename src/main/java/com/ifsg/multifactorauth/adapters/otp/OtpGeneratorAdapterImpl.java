package com.ifsg.multifactorauth.adapters.otp;

import com.ifsg.multifactorauth.config.OTPPolicyConfig;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.stereotype.Service;

@Service
public class OtpGeneratorAdapterImpl implements OTPGeneratorAdapter {
    private final OTPPolicyConfig otpPolicyConfig;

    public OtpGeneratorAdapterImpl(OTPPolicyConfig otpPolicyConfig) {
        this.otpPolicyConfig = otpPolicyConfig;
    }

    @Override
    public String generateOTP() throws CodeGenerationException {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, this.otpPolicyConfig.getCodeLength());
        long currentBucket = Math.floorDiv(timeProvider.getTime(), this.otpPolicyConfig.getCodeExpiry());
        return codeGenerator.generate(otpPolicyConfig.getCodeSecret(), currentBucket);
    }

    @Override
    public boolean verifyOTP(String otp) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(otpPolicyConfig.getCodeSecret(), otp);
    }

    @Override
    public String generateSecret(int length) {
        SecretGenerator secretGenerator = new DefaultSecretGenerator(length);
        return secretGenerator.generate();
    }
}
