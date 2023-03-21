package com.test.service;

import com.test.dto.TopSearchedRespDto;
import com.test.search.entity.Keywords;
import com.test.search.repository.KeywordsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TopSearchedService.class})
class TopSearchedServiceTest {

    @Autowired
    private TopSearchedService topSearchedService;

    @MockBean
    private KeywordsRepository keywordsRepository;

    @Test
    @DisplayName("인기검색어 호출 테스트")
    public void fetchTopSearchedListTest() {
        // given
        Keywords keyword1 = Keywords.builder()
                .keyword("example1")
                .count(5L)
                .build();

        Keywords keyword2 = Keywords.builder()
                .keyword("example2")
                .count(3L)
                .build();

        // when
        List<Keywords> keywordsList = Arrays.asList(keyword1, keyword2);

        when(keywordsRepository.findTop10ByOrderByCountDesc()).thenReturn(keywordsList);

        List<TopSearchedRespDto> result = topSearchedService.fetchTopSearchedList();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getKeyword()).isEqualTo("example1");
        assertThat(result.get(0).getCount()).isEqualTo(5);
        assertThat(result.get(1).getKeyword()).isEqualTo("example2");
        assertThat(result.get(1).getCount()).isEqualTo(3);

        verify(keywordsRepository, times(1)).findTop10ByOrderByCountDesc();
    }
}