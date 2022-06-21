package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "CTKIP")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TokenCTKIP {
    @XmlAttribute(name = "deviceFamilyName")
    private String deviceFamilyName;
}
