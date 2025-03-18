package com.example.YONDU.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "identifier")
        })
public class Member {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String identifier;


    @NotBlank
    @Size(max = 120)
    @Getter
    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Getter
    @Setter
    private ERole role;

    public Member() {
    }

    public Member(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }
}