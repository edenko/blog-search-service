package com.test.search.repository;

import com.test.search.entity.Keywords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordsRepository extends JpaRepository<Keywords, Long> {
    Optional<Keywords> findById(Long id);

    Optional<Keywords> findTopByKeyword(String keyword);

    List<Keywords> findTop10ByOrderByCountDesc();
}
