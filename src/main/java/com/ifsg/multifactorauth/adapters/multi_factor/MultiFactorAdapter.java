package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.exceptions.InvalidInputException;
import com.ifsg.multifactorauth.models.enums.AuthMethod;
import com.ifsg.multifactorauth.models.interfaces.IMultiFactorAuthAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MultiFactorAdapter {
    private final RSAAdapter rsaAdapter;
    private final VeridiumAdapter veridiumAdapter;
    private final SMSOTPAdapter SMSOTPAdapter;
    private final EmailOTPAdapter emailOTPAdapter;

    public IMultiFactorAuthAdapter getAdapter(AuthMethod method) {
        switch (method) {
            case SMS_CODE -> { return this.SMSOTPAdapter; }

            case EMAIL_CODE -> { return this.emailOTPAdapter; }

            case SECURITY_QUESTION, VOICE_CODE, PIN_CODE -> { return null; }

            case RSA_SECUREID -> { return this.rsaAdapter; }

            case VERIDIUM -> { return this.veridiumAdapter; }

            default -> { throw new InvalidInputException("Invalid Auth Method"); }
        }
    }
}
