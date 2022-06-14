package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "authenticationResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthenticationResult {
    @XmlAttribute(name="authenticated")
    private boolean authenticated;

    @XmlAttribute(name="message")
    private String message;

    @XmlAttribute(name="code")
    private int code;

    @XmlAttribute(name="failed")
    private boolean failed;

    @XmlAttribute(name = "authenticationToken")
    private String authenticationToken;

    @XmlAttribute(name = "hasQuestions")
    private boolean hasQuestions;

    @XmlAttribute(name = "publicID")
    private String publicID;
}
