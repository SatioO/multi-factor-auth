package com.ifsg.multifactorauth.rest.rsa;

import com.ifsg.multifactorauth.config.RSAPolicyConfig;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.rest.rsa.models.*;
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
        this.webClient = webClientBuilder
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
        String authenticationStr = sw.toString();

        return restTemplate
                .exchange("http://10.1.7.214:8080/auth/authn",HttpMethod.POST, new HttpEntity<String>(authenticationStr, headers), AuthenticationResult.class);
    }

    public ResponseEntity<ServiceResult> createUser(String authToken, CreateUserBodyDTO data) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.set("RSA_AUTHENTICATION_TOKEN", authToken);

        UserEntry user = new UserEntry();
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setEmailAddress(data.getPrimaryEmail());
        user.setEnabled(true);

        StringWriter sw = new StringWriter();
        JAXB.marshal(user, sw);
        String userStr = sw.toString();

        return restTemplate
                .exchange("http://10.1.7.214:8080/am8/user/create/" + data.getExternalId(), HttpMethod.PUT, new HttpEntity<String>(userStr, headers), ServiceResult.class);
    }
}
