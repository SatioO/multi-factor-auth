package com.ifsg.multifactorauth.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ifsg.multifactorauth.models.enums.UserStatus;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "MULTI_FACTOR_USERS")
public class UserEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "challengeId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type="uuid-char")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String primaryEmail;

    @Column(nullable = true)
    private String secondaryMail;

    @Column(nullable = false)
    private String phoneCountryCode;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = true)
    @JsonIgnore
    private String smsToken;

    @Column(nullable = true)
    @JsonIgnore
    private String emailToken;

    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "created_time", nullable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;
}
