package com.example.YONDU.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;  // ex) "testUser"
    private String password;

    private String name;
    private String phone;
    private boolean banned;
    // ... 기타 필드

    // 필드나 메서드는 필요에 따라 작성
}
