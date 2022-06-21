package com.ifsg.multifactorauth.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ifsg.multifactorauth.models.enums.TokenStatus;
import com.ifsg.multifactorauth.models.enums.TokenType;
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
@Entity(name = "USER_SOFT_TOKEN")
public class UserSoftTokenEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "tokenId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type="uuid-char")
    private UUID id;

    @Column(nullable = false)
    private String externalId;

    @Column(nullable = false)
    private TokenType type;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String token;

    @Column(nullable = false)
    private TokenStatus status;

    @Column(name = "created_time", nullable = false, updatable = false)
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;
}
