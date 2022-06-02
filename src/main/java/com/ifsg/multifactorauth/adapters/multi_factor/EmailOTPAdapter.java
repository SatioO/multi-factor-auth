package com.ifsg.multifactorauth.adapters.multi_factor;

import com.ifsg.multifactorauth.adapters.otp.OTPGeneratorAdapter;
import com.ifsg.multifactorauth.entities.AuthSessionEntity;
import com.ifsg.multifactorauth.models.dtos.CreateSessionDTO;
import com.ifsg.multifactorauth.models.dtos.SessionDTO;
import com.ifsg.multifactorauth.models.dtos.ValidateSessionDTO;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.models.interfaces.MultiFactorAuth;
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
    public SessionDTO createSession(CreateSessionDTO data) {
        String code = otpGeneratorAdapter.generate(codeLength);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MILLISECOND, codeExpiry);

        return SessionDTO.builder()
                .code(code)
                .method(data.getAuthMethod())
                .status(AuthStatus.PENDING)
                .expiryTime(calendar.getTime())
                .createdTime(new Date())
                .build();
    }

    @Override
    public Boolean validateSession(AuthSessionEntity entity, ValidateSessionDTO body) {
        return entity.getCode().equals(body.getCode());
    }
}
