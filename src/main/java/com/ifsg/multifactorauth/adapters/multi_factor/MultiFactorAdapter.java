package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.models.enums.AuthMethod;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuth;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MultiFactorAdapter {
    private final RSAAdapter rsaAdapter;
    private final VeridiumAdapter veridiumAdapter;
    private final PhoneOTPAdapter phoneOTPAdapter;
    private final EmailOTPAdapter emailOTPAdapter;

    public MultiFactorAuth getAdapter(AuthMethod method) {
        switch (method) {
            case SMS_CODE -> { return this.phoneOTPAdapter; }

            case EMAIL_CODE -> { return this.emailOTPAdapter; }

            case PASSWORD -> { return null; }

            case PIN_CODE -> { return null; }

            case EMERGENCY_CODE -> { return null; }

            case VOICE_CODE -> { return null; }

            case RSA_SECUREID -> { return this.rsaAdapter; }

            case VERIDIUM -> { return this.veridiumAdapter; }

            default -> { throw new RuntimeException("Invalid Auth Method"); }
        }
    }
}
