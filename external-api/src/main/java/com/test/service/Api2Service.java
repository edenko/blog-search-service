package com.test.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class Api2Service {
    @Value("${naver.key}")
    protected String key;
    @Value("${naver.key2}")
    protected String key2;
    @Value("${naver.url}")
    protected String url;
    @Value("${naver.blog.url}")
    protected String blog;

    @Async("customExecutor")
    public CompletableFuture<Map> fetchApi2Data(
            String query,
            String sort,
            Pageable pageable
    ) {
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path(blog)
                .queryParam("query", query)
                .queryParam("sort", sort.trim())
                .queryParam("start", pageable.getPageNumber() + 1)
                .queryParam("display", pageable.getPageSize())
                .encode()
                .build()
                .toUri();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", key)
                .header("X-Naver-Client-Secret", key2)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        return CompletableFuture.completedFuture(restTemplate.exchange(uri, HttpMethod.GET, req, Map.class).getBody());
    }
}
