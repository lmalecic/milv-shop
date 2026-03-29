package com.lmalecic.milvshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table
@Entity
public final class Nation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imgPath;
}
