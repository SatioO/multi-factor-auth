package com.ifsg.multifactorauth.rest.veridium.models;

import lombok.Data;

@Data
public class VeridiumSession {
    private String userId;
    private int id;
    private String title;
    private boolean completed;
}
