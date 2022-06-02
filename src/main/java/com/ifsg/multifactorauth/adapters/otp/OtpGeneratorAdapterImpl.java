package com.ifsg.multifactorauth.adapters.otp;

import org.springframework.stereotype.Service;

@Service
public class OtpGeneratorAdapterImpl implements OTPGeneratorAdapter {
    @Override
    public String generate(int length) {
        return "123456";
    }
}
