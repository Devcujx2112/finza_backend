package com.finza.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long categoriesId;

    private Long userId;

    private String categoriesName;

    private String stringIcon;
}
