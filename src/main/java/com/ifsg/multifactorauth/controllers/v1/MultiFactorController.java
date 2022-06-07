package com.ifsg.multifactorauth.controllers.v1;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.ChallengeResponse;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.services.MultiFactorService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/authn")
public class MultiFactorController {
    private final MultiFactorService multiFactorService;

    public MultiFactorController(MultiFactorService multiFactorService) {
        this.multiFactorService = multiFactorService;
    }

    @GetMapping("/status/{challengeId}")
    public MultiFactorEntity getChallengeStatus(@PathVariable UUID challengeId, @RequestHeader Map<String, String> headers) {
        return this.multiFactorService.getChallengeStatus(challengeId);
    }

    @PostMapping("/verify")
    public ChallengeResponse verifyChallenge(@RequestHeader Map<String, String> headers, @Valid @RequestBody VerifyChallengeDTO body) {
        return this.multiFactorService.verifyChallenge(body);
    }

    @PostMapping("/initialize")
    public MultiFactorEntity initializeChallenge(@RequestHeader Map<String, String> headers, @Valid @RequestBody InitializeChallengeDTO body, Authentication auth) {
        return this.multiFactorService.initializeChallenge(headers, body, auth);
    }

    @PostMapping("/cancel/{challengeId}")
    public MultiFactorEntity cancelChallenge(@PathVariable UUID challengeId) {
        return null;
    }
}
