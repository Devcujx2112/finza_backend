package com.finza.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    //Khoa chinh
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Tu tang
    @Column(unique = true)
    private Long userId;

    @Column(unique = true)
    //Khong duoc trung
    private String email;

    @Column(unique = true)
    private String googleId;

    @Column(unique = true)
    private String facebookId;

    @Column(unique = true)
    private String appleId;

    @Enumerated(EnumType.STRING)
    private SocialType provider;

    private LocalDate trialExpiredAt;

    @Enumerated(EnumType.STRING)
    private AccountTier accountTier;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    private String fullName;

    private String phoneNumber;

    private String password;

    private String dateOfBirth;

    private String urlAvatar;

    @Column(nullable = false)
    private boolean loginWithBioMetric;

    private String created_at;
}
