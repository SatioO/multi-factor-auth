package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "property")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TokenProperty {
    @XmlAttribute(name = "requirePintAtNextLogin")
    private boolean requirePintAtNextLogin;

    @XmlAttribute(name = "action")
    private String action;

    @XmlAttribute(name = "pinType")
    private String pinType;
}
