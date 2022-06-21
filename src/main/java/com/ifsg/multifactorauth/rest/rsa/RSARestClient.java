package com.ifsg.multifactorauth.rest.rsa;

import com.ifsg.multifactorauth.config.RSAPolicyConfig;
import com.ifsg.multifactorauth.models.dtos.CreateUserBodyDTO;
import com.ifsg.multifactorauth.models.enums.DeviceType;
import com.ifsg.multifactorauth.rest.rsa.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXB;
import java.io.StringWriter;

@Slf4j
@Service
public class RSARestClient {
    private final RSAPolicyConfig rsaPolicyConfig;

    public RSARestClient(RSAPolicyConfig rsaPolicyConfig) {
        this.rsaPolicyConfig = rsaPolicyConfig;
    }

    public ResponseEntity<AuthenticationResult> getAuthToken() {
        RestTemplate restTemplate = new RestTemplate();

        ServiceAccount serviceAccount = new ServiceAccount();
        serviceAccount.setUserId(rsaPolicyConfig.getConnection().getUsername());
        serviceAccount.setPassword(rsaPolicyConfig.getConnection().getPassword());

        Authentication authentication = new Authentication();
        authentication.setType(rsaPolicyConfig.getConnection().getType());
        authentication.setProfile(rsaPolicyConfig.getConnection().getProfile());
        authentication.setServiceAccount(serviceAccount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        StringWriter sw = new StringWriter();
        JAXB.marshal(authentication, sw);
        String authenticationStr = sw.toString();

        return restTemplate
                .exchange(rsaPolicyConfig.getConnection().getBaseUrl() + "/auth/authn",HttpMethod.POST, new HttpEntity<String>(authenticationStr, headers), AuthenticationResult.class);
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
                .exchange(rsaPolicyConfig.getConnection().getBaseUrl() + "/am8/user/create/" + data.getExternalId(), HttpMethod.PUT, new HttpEntity<String>(userStr, headers), ServiceResult.class);
    }

    public ResponseEntity<ServiceResult> assignToken(String authToken, String externalId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.set("RSA_AUTHENTICATION_TOKEN", authToken);

        return restTemplate
                .exchange(rsaPolicyConfig.getConnection().getBaseUrl() + "/am8/user/assignNext/"+ externalId +"/software", HttpMethod.GET, new HttpEntity<String>(null, headers), ServiceResult.class);
    }

    public ResponseEntity<Boolean> generateQR(DeviceType deviceType, String deviceId) {
        TokenCTKIP tokenCTKIP = new TokenCTKIP();
        tokenCTKIP.setDeviceFamilyName(deviceType == DeviceType.IPHONE ? "Iphone": "Android");

        TokenQR tokenQR = new TokenQR();
        tokenQR.setSize(300);

        TokenDistribution distribution = new TokenDistribution();
        distribution.setCtkip(tokenCTKIP);
        distribution.setQr(tokenQR);

        TokenPin tokenPin = new TokenPin();
        tokenPin.setName("DeviceSerialNumber");
        tokenPin.setValue("pinless");

        TokenProperty tokenProperty = new TokenProperty();
        tokenProperty.setAction("N/A");
        tokenProperty.setPinType("pinless");
        tokenProperty.setRequirePintAtNextLogin(false);

        TokenProperties tokenProperties = new TokenProperties();
        tokenProperties.setClearValues(false);
        tokenProperties.setProperty(tokenProperty);

        TokenEntry token = new TokenEntry();
        token.setEnabled(true);
        token.setAlgorithm("time");
        token.setDeviceType(deviceId);
        token.setInterval(rsaPolicyConfig.getCodeExpiry());
        token.setTokenCodeLength(rsaPolicyConfig.getCodeLength());
        token.setDistribution(distribution);
        token.setTokenPin(tokenPin);
        token.setTokenProperties(tokenProperties);

        StringWriter sw = new StringWriter();
        JAXB.marshal(token, sw);
        String tokenStr = sw.toString();
        System.out.println(tokenStr);
        return null;
    }
}
