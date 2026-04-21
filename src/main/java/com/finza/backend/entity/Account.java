package com.finza.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    //Khoa chinh
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Tu tang
    @Column(unique = true)
    private Long user_id;

    @Column(unique = true)
    //Khong duoc trung
    private String userName;

    @Enumerated(EnumType.STRING)
    private AccountTier accountTier;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    private String fullName;

    private String phoneNumber;

    private String password;

    private String dateOfBirth;

    private String urlAvatar;
}
