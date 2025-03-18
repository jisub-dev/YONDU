package com.example.YONDU.payload.response;

import java.util.List;

public class MemberInfoResponse {
    private String identifier;
    private List<String> roles;

    public MemberInfoResponse(String identifier, List<String> roles) {
        this.identifier = identifier;
        this.roles = roles;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<String> getRoles() {
        return roles;
    }
}
