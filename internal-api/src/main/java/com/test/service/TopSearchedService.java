package com.test.service;

import com.test.dto.TopSearchedRespDto;
import com.test.search.entity.Keywords;
import com.test.search.error.CustomExceptionHandler;
import com.test.search.repository.KeywordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.test.search.error.Constants.ExceptionClass.SEARCH;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TopSearchedService {

    private final KeywordsRepository keywordsRepository;

    public List<TopSearchedRespDto> fetchTopSearchedList() {
        try {
            List<Keywords> items = keywordsRepository.findTop10ByOrderByCountDesc();

            if (items.isEmpty()) {
                throw new CustomExceptionHandler(SEARCH, NOT_FOUND, "No data found");
            }

            return items.stream()
                    .map(item -> TopSearchedRespDto.builder()
                            .keyword(item.getKeyword())
                            .count(item.getCount())
                            .build())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
