package com.lmalecic.milvshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Nation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    private String imgPath;

    @Builder.Default
    @ColumnDefault("false")
    @Column(nullable = false)
    private boolean deleted = false;
}
