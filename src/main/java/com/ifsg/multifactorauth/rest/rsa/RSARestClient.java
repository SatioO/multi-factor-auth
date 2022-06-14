package com.ifsg.multifactorauth.rest.rsa;

import com.ifsg.multifactorauth.config.RSAPolicyConfig;
import com.ifsg.multifactorauth.rest.rsa.models.Authentication;
import com.ifsg.multifactorauth.rest.rsa.models.AuthenticationResult;
import com.ifsg.multifactorauth.rest.rsa.models.ServiceAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.bind.JAXB;
import java.io.StringWriter;

@Slf4j
@Service
public class RSARestClient {

    private final WebClient webClient;
    private final RSAPolicyConfig rsaPolicyConfig;


    public RSARestClient(WebClient.Builder webClientBuilder, RSAPolicyConfig rsaPolicyConfig) {
        this.webClient = webClientBuilder // you can also just use WebClient.builder()
                .baseUrl(rsaPolicyConfig.getBaseUrl())
                .build();

        this.rsaPolicyConfig = rsaPolicyConfig;
    }

    public ResponseEntity<AuthenticationResult> getAuthToken() {
        RestTemplate restTemplate = new RestTemplate();

        ServiceAccount serviceAccount = new ServiceAccount();
        serviceAccount.setUserId(rsaPolicyConfig.getUsername());
        serviceAccount.setPassword(rsaPolicyConfig.getPassword());

        Authentication authentication = new Authentication();
        authentication.setType(rsaPolicyConfig.getType());
        authentication.setProfile(rsaPolicyConfig.getProfile());
        authentication.setServiceAccount(serviceAccount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        StringWriter sw = new StringWriter();
        JAXB.marshal(authentication, sw);
        String xmlString = sw.toString();

        HttpEntity<String> requestInfo = new HttpEntity<String>(xmlString, headers);

        return restTemplate
                .postForEntity("http://10.1.7.214:8080/auth/authn", requestInfo, AuthenticationResult.class);
    }
}
