package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.adapters.multi_factor.MultiFactorAdapter;
import com.ifsg.multifactorauth.entities.AuthSessionEntity;
import com.ifsg.multifactorauth.models.dtos.CreateSessionDTO;
import com.ifsg.multifactorauth.models.dtos.SessionDTO;
import com.ifsg.multifactorauth.models.dtos.ValidateSessionDTO;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.repositories.AuthSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private MultiFactorAdapter multiFactorAdapter;

    @Autowired
    private AuthSessionRepository authSessionRepository;

    @Value("${multi_factor.modes.email_otp.code_limit}")
    private Integer codeLimit;

    @Override
    public AuthSessionEntity getSessionById(UUID sessionId) {
        return this.authSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session Not Found."));
    }

    @Override
    public Boolean validateSession(ValidateSessionDTO body) {
        AuthSessionEntity entity = this.authSessionRepository.findById(body.getSessionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Session Found."));

        if (entity.getStatus() == AuthStatus.SUCCESS) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Session is already verified");
        }

        if (entity.getStatus() == AuthStatus.BLOCKED) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Session is blocked for verification. Please try creating a new session.");
        }

        Boolean result = this.multiFactorAdapter
                .getAdapter(body.getAuthMethod())
                .validateSession(entity, body);

        Integer attempts = entity.getAttempts() + 1;

        if(result) {
            Date currentDate = new Date();
            boolean hasExceededExpiryTime = currentDate.after(entity.getExpiryTime());

            this.authSessionRepository.save(
                    entity.toBuilder()
                            .attempts(attempts)
                            .status(hasExceededExpiryTime ? AuthStatus.FAILURE : AuthStatus.SUCCESS)
                            .build());

            if(hasExceededExpiryTime) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE,
                        "Current Session is Expired"
                );
            }

            return true;
        } else {
            boolean hasExceededCodeLimit = attempts >= codeLimit;
            this.authSessionRepository.save(
                    entity.toBuilder()
                            .attempts(attempts)
                            .status(hasExceededCodeLimit ? AuthStatus.BLOCKED : AuthStatus.FAILURE)
                            .build());

            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    hasExceededCodeLimit ? "Current Session is Blocked" : "Invalid Code"
            );
        }
    }

    @Override
    public AuthSessionEntity createSession(CreateSessionDTO body) {
        SessionDTO session = this.multiFactorAdapter
                .getAdapter(body.getAuthMethod())
                .createSession(body);

        return this.authSessionRepository.save(
                AuthSessionEntity
                        .builder()
                        .code(session.getCode())
                        .customerId("DUMMY_CUST_1")
                        .channel("DUMMY_CHAN_WEB")
                        .ipAddress("1.2.3.4")
                        .method(session.getMethod())
                        .attempts(0)
                        .status(session.getStatus())
                        .createdTime(session.getCreatedTime())
                        .updatedTime(session.getCreatedTime())
                        .expiryTime(session.getExpiryTime())
                        .browserVersion(body.getBrowserVersion())
                        .appVersion(body.getDeviceVersion())
                        .build()
        );
    }
}
