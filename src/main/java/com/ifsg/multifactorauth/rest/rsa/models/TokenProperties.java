package com.ifsg.multifactorauth.rest.rsa.models;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "properties")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class TokenProperties {
    @XmlAttribute(name = "clearValues")
    private boolean clearValues;

    @XmlElement(name = "property")
    private TokenProperty property;
}
