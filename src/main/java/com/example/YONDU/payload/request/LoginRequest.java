package com.example.YONDU.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class LoginRequest {
    @NotBlank
    @Getter
    @Setter
    private String identifier;

    @NotBlank
    @Getter
    @Setter
    private String password;
}
