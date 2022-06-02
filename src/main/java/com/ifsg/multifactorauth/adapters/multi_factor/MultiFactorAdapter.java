package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.models.enums.AuthMethod;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuth;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MultiFactorAdapter {
    private final VeridiumAdapter veridiumAdapter;
    private final RSAAdapter rsaAdapter;
    private final PhoneOTPAdapter phoneOTPAdapter;
    private final EmailOTPAdapter emailOTPAdapter;

    public MultiFactorAuth getAdapter(AuthMethod method) {
        switch (method) {
            case VERIDIUM -> {
                return this.veridiumAdapter;
            }

            case RSA -> {
                return this.rsaAdapter;
            }

            case PHONE_OTP -> {
                return this.phoneOTPAdapter;
            }

            case EMAIL_OTP -> {
                return this.emailOTPAdapter;
            }

            default -> {
                throw new RuntimeException("Invalid Auth Method");
            }
        }
    }
}
