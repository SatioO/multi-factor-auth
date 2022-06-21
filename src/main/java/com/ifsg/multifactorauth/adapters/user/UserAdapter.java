package com.ifsg.multifactorauth.adapters.user;

import com.ifsg.multifactorauth.exceptions.InvalidInputException;
import com.ifsg.multifactorauth.models.enums.AuthMethod;
import com.ifsg.multifactorauth.models.interfaces.IUserAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAdapter {
    private final EmailUserAdapter emailUserAdapter;
    private final SMSUserAdapter smsUserAdapter;
    private final RSAUserAdapter rsaUserAdapter;
    private final VeridiumUserAdapter veridiumUserAdapter;

    public IUserAdapter getAdapter(AuthMethod method) {
        switch (method) {
            case EMAIL_CODE -> {
                return emailUserAdapter;
            }

            case SMS_CODE -> {
                return smsUserAdapter;
            }

            case RSA_SECURID -> {
                return rsaUserAdapter;
            }

            case VERIDIUM -> {
                return veridiumUserAdapter;
            }

            default -> { throw new InvalidInputException("Invalid Auth Method"); }
        }
    }
}
