package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@Data
@XmlRootElement(name = "tokenEntry")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TokenEntry {
    @XmlElement(name = "enabled")
    private boolean enabled;

    @XmlElement(name = "distribution")
    private TokenDistribution distribution;

    @XmlElement(name = "deviceType")
    private String deviceType;

    @XmlElement(name = "algorithm")
    private String algorithm;

    @XmlElement(name = "tokenCodeLength")
    private int tokenCodeLength;

    @XmlElement(name = "interval")
    private int interval;

    @XmlElement(name = "properties")
    private TokenProperties tokenProperties;

    @XmlElement(name = "pin")
    private TokenPin tokenPin;
}

