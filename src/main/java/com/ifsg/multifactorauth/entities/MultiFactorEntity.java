package com.ifsg.multifactorauth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ifsg.multifactorauth.models.enums.AuthMethod;
import com.ifsg.multifactorauth.models.enums.AuthReasonCode;
import com.ifsg.multifactorauth.models.enums.AuthStatus;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "MULTI_FACTOR_AUTH_SESSION")
public class MultiFactorEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "challengeId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type="uuid-char")
    private UUID challengeId;

    @Column(nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type="uuid-char")
    private UUID sessionId;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String channel;

    @Column(nullable = true)
    @JsonIgnore
    private String deviceId;

    @Column(nullable = true)
    @JsonIgnore
    private String appVersion;

    @Column(nullable = true)
    @JsonIgnore
    private String browserVersion;

    @Column(nullable = true)
    @JsonIgnore
    private String ipAddress;

    @Column(nullable = true)
    @JsonIgnore
    private String code;

    @Column(name = "created_time", nullable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(nullable = false)
    @JsonIgnore
    private Date expiryTime;

    @Column(nullable = false)
    @JsonIgnore
    private Integer attempts;

    @Column(nullable = false)
    private AuthMethod authMethod;

    @Column(nullable = true)
    private AuthReasonCode authReasonCode;

    @Column(nullable = false)
    private AuthStatus authStatus;
}