package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "serviceResult")
@Data
public class ServiceResult {
    @XmlAttribute(name = "result")
    private boolean result;

    @XmlAttribute(name = "errorMessage")
    private String errorMessage;

    @XmlElement(name = "TokenSerialNumber")
    private String tokenSerialNumber;
}
