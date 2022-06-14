package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "serviceAccount")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceAccount {
    @XmlAttribute(name = "userId")
    private String userId;

    @XmlAttribute(name = "password")
    private String password;
}
