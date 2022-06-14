package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.exceptions.InvalidInputException;
import com.ifsg.multifactorauth.models.enums.AuthMethod;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAdapter {
    private final OTPUserAdapter otpUserAdapter;
    private final RSAUserAdapter rsaUserAdapter;
    private final VeridiumUserAdapterAdapter veridiumUserAdapter;

    public com.ifsg.multifactorauth.models.interfaces.UserAdapter getAdapter(AuthMethod method) {
        switch (method) {
            case SMS_CODE, EMAIL_CODE -> {
                return otpUserAdapter;
            }

            case RSA_SECUREID -> {
                return rsaUserAdapter;
            }

            case VERIDIUM -> {
                return veridiumUserAdapter;
            }

            default -> { throw new InvalidInputException("Invalid Auth Method"); }
        }
    }
}
