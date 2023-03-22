package com.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Api1ServiceTest {

    @InjectMocks
    private Api1Service api1Service;

    // unsafe
    @BeforeEach
    void setUp() {
        api1Service.key = "a3d8c54dc1cee46687223caa3a8e4aff";
        api1Service.url = "https://dapi.kakao.com";
        api1Service.blog = "/v2/search/blog";
    }

    @Test
    @DisplayName("API 1 Call Test")
    void testFetchApi1Data() {
        Map result = api1Service.fetchApi1Data("더글로리", "accuracy", PageRequest.of(0, 10)).join();

        assertNotNull(result);
        assertTrue(result.containsKey("documents"));
        List<Map<String, Object>> documents = (List<Map<String, Object>>) result.get("documents");
        assertFalse(documents.isEmpty());
        documents.forEach(document -> {
            assertTrue(document.containsKey("blogname"));
            assertTrue(document.containsKey("contents"));
        });
    }
}
