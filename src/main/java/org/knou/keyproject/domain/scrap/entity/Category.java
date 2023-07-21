package org.knou.keyproject.domain.scrap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.knou.keyproject.global.audit.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

// 2023.7.22(토) 1h5
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<Scrap> scrapList = new ArrayList<>();
}
