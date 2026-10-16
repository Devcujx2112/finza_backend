package com.finza.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "image_path")
@Data
public class ImagePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long imageId;

    private String imageName;

    private String imageUrl;
}
