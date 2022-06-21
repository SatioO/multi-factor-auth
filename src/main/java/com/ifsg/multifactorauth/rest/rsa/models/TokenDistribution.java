package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "distribution")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TokenDistribution {
    @XmlElement(name = "CTKIP")
    private TokenCTKIP ctkip;

    @XmlElement(name = "QR")
    private TokenQR qr;
}
