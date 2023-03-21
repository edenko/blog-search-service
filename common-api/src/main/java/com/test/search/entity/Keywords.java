package com.test.search.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Keywords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String keyword;

    @ColumnDefault("0")
    private Long count;

    public void updateKeyWord (String keyword, long count) {
        this.keyword = keyword;
        this.count = count;
    }
}

