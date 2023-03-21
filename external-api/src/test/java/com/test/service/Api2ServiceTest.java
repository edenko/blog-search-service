package com.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Api2ServiceTest {

    @InjectMocks
    private Api2Service api2Service;

    @BeforeEach
    void setUp() {
        api2Service.key = "73LmIgZj7eSJkaWEdjor";
        api2Service.key2 = "kI5nHBpg4d";
        api2Service.url = "https://openapi.naver.com";
        api2Service.blog = "/v1/search/blog.json";
    }

    @Test
    @DisplayName("API 2 Call Test")
    void testFetchApi2Data() {
        Map result = api2Service.fetchApi2Data("더글로리", "sim", PageRequest.of(0, 10)).join();

        assertNotNull(result);
        assertTrue(result.containsKey("items"));
        List<Map<String, Object>> documents = (List<Map<String, Object>>) result.get("items");
        assertFalse(documents.isEmpty());
    }
}
