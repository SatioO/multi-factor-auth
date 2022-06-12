package com.ifsg.multifactorauth.adapters.otp;

import dev.samstevens.totp.exceptions.CodeGenerationException;

public interface OTPGeneratorAdapter {
    String generateOTP(String secret) throws CodeGenerationException;

    boolean verifyOTP(String otp);

    String generateSecret(int length);
}
