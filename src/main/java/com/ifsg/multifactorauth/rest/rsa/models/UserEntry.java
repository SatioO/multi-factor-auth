package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "userEntry")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserEntry {
    @XmlElement(name = "emailAddress")
    private String emailAddress;

    @XmlElement(name = "firstName")
    private String firstName;

    @XmlElement(name = "lastName")
    private String lastName;

    @XmlElement(name = "enabled")
    private boolean enabled;
}
