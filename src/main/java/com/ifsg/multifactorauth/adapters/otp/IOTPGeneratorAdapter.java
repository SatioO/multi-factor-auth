package com.ifsg.multifactorauth.adapters.otp;

import dev.samstevens.totp.exceptions.CodeGenerationException;

public interface IOTPGeneratorAdapter {
    String generateOTP(String secret) throws CodeGenerationException;

    boolean verifyOTP(String secret, String otp);

    String generateSecret(int length);
}
