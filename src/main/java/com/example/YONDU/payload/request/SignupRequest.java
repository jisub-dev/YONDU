package com.example.YONDU.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    @Getter
    @Setter
    private String identifier;

    @Getter
    @Setter
    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    @Getter
    @Setter
    private String password;
}