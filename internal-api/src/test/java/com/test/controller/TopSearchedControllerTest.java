package com.test.controller;

import com.test.service.TopSearchedService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TopSearchedController.class)
class TopSearchedControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TopSearchedService topSearchedService;

    @Test
    @DisplayName("인기검색어 컨트롤러 테스트")
    void fetchTopSearchedList() throws Exception {
        when(topSearchedService.fetchTopSearchedList()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/top/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }
}