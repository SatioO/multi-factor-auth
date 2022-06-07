package com.ifsg.multifactorauth.controllers.v1;

import com.ifsg.multifactorauth.entities.MultiFactorEntity;
import com.ifsg.multifactorauth.models.dtos.ChallengeResponse;
import com.ifsg.multifactorauth.models.dtos.InitializeChallengeDTO;
import com.ifsg.multifactorauth.models.dtos.VerifyChallengeDTO;
import com.ifsg.multifactorauth.services.MultiFactorService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/authn")
@AllArgsConstructor
public class MultiFactorController {
    private final MultiFactorService multiFactorService;

    @GetMapping("/status/{sessionId}")
    public MultiFactorEntity getChallengeStatus(@PathVariable UUID sessionId, @RequestHeader Map<String, String> headers) {
        return this.multiFactorService.getChallengeStatus(sessionId);
    }

    @PostMapping("/verify")
    public ChallengeResponse verifyChallenge(@RequestHeader Map<String, String> headers, @Valid @RequestBody VerifyChallengeDTO body) {
        return this.multiFactorService.verifyChallenge(body);
    }

    @PostMapping("/initialize")
    public MultiFactorEntity initializeChallenge(@RequestHeader Map<String, String> headers, @Valid @RequestBody InitializeChallengeDTO body, Authentication auth) {
        return this.multiFactorService.initializeChallenge(headers, body, auth);
    }

    @PostMapping("/cancel")
    public MultiFactorEntity cancelChallenge() {
        return null;
    }
}