package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@Data
@XmlRootElement(name = "Authentication")
@XmlAccessorType(XmlAccessType.NONE)
public class Authentication implements Serializable {
    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "profile")
    private String profile;

    @XmlElement(name = "serviceAccount")
    private ServiceAccount serviceAccount;
}

