package com.ifsg.multifactorauth.services;

import com.ifsg.multifactorauth.adapters.multi_factor.MultiFactorAdapter;
import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.ChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import com.ifsg.multifactorauth.repositories.MultiFactorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
public class MultiFactorServiceImpl implements MultiFactorService {
    @Autowired
    private MultiFactorAdapter multiFactorAdapter;

    @Autowired
    private MultiFactorRepository multiFactorRepository;

    @Value("${multi_factor.modes.email_otp.code_limit}")
    private Integer codeLimit;

    @Override
    public MultiFactorEntity getChallengeStatus(UUID sessionId) {
        return this.multiFactorRepository
                .findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session Not Found."));
    }

    @Override
    public Boolean verifyChallenge(VerifyChallengeDTO body) {
        MultiFactorEntity entity = this.multiFactorRepository.findById(body.getSessionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Session Found."));

        // NOTE: Return error if the user tries to validate already utilised session
        if (entity.getStatus() == AuthStatus.SUCCESS) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Current Session is already verified");
        }

        // NOTE: Return error if the user tries to validate blocked session
        if (entity.getStatus() == AuthStatus.ERROR) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "The server cannot handle the request. The request is malformed or invalid."
            );
        }

        // NOTE: Return error if the user tries to validate blocked session
        if (entity.getStatus() == AuthStatus.EXPIRED) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Current Session is Expired. Please try creating a new session.");
        }

        Integer attempts = entity.getAttempts() + 1;

        // NOTE: check if current session has exceeded the expiry time
        Date currentDate = new Date();
        boolean hasExceededExpiryTime = currentDate.after(entity.getExpiryTime());

        // NOTE: if it has crossed the expiry time
        if(hasExceededExpiryTime) {
            this.multiFactorRepository.save(
                    entity.toBuilder()
                            .attempts(attempts)
                            .status(AuthStatus.EXPIRED)
                            .build());

            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Current Session is Expired. Please try creating a new session."
            );
        }

        // NOTE: if it crosses set limit, then mark that as blocked
        boolean hasExceededCodeLimit = attempts > codeLimit;
        if(hasExceededCodeLimit) {
            this.multiFactorRepository.save(
                    entity.toBuilder()
                            .attempts(attempts)
                            .status(AuthStatus.ERROR)
                            .build());

            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Access denied for this request. You might have entered the wrong credential."
            );
        }

        // NOTE: Connect to respective adapter to perform validation logic such as compare OTP
        Boolean result = this.multiFactorAdapter
                .getAdapter(body.getAuthMethod())
                .validateSession(entity, body);

        // NOTE: if entered OTP matches, mark it with success in DB
        if(result) {
            this.multiFactorRepository.save(
                    entity.toBuilder()
                            .attempts(attempts)
                            .status(AuthStatus.SUCCESS)
                            .build());
            return true;
        } else {
            // NOTE: if entered OTP doesn't match, mark it with failure in DB but if it crosses set limit, then mark that as blocked
            this.multiFactorRepository.save(
                    entity.toBuilder()
                            .attempts(attempts)
                            .status(AuthStatus.FAIL)
                            .build());

            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Access denied for this request. You might have entered the wrong credential."
            );
        }
    }

    @Override
    public MultiFactorEntity initializeChallenge(InitializeChallengeDTO body) {
        ChallengeDTO session = this.multiFactorAdapter
                .getAdapter(body.getAuthMethod())
                .createSession(body);

        return this.multiFactorRepository.save(
                MultiFactorEntity
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
