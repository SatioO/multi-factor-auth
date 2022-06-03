package com.ifsg.multifactorauth.controllers.v1;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.ChallengeResponse;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.services.MultiFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/authn")
public class MultiFactorController {
    @Autowired
    private MultiFactorService multiFactorService;

    @GetMapping("/status")
    public MultiFactorEntity getChallengeStatus(UUID sessionId) {
        return this.multiFactorService.getChallengeStatus(sessionId);
    }

    @PostMapping("/verify")
    public ChallengeResponse verifyChallenge(@Valid @RequestBody VerifyChallengeDTO body) {
        return this.multiFactorService.verifyChallenge(body);
    }

    @PostMapping("/initialize")
    public MultiFactorEntity initializeChallenge(@Valid @RequestBody InitializeChallengeDTO body) {
        return this.multiFactorService.initializeChallenge(body);
    }

    @PostMapping("/cancel")
    public MultiFactorEntity cancelChallenge() {
        return null;
    }
}
