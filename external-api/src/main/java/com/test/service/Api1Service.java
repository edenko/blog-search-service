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
public class Api1Service {
    @Value("${kakao.key}")
    protected String key;
    @Value("${kakao.url}")
    protected String url;
    @Value("${kakao.blog.url}")
    protected String blog;

    @Async("customExecutor")
    public CompletableFuture<Map> fetchApi1Data(
            String query,
            String sort,
            Pageable pageable
    ) {
        URI uri = UriComponentsBuilder.fromUriString(url)
                .path(blog)
                .queryParam("query", query)
                .queryParam("sort", sort.trim())
                .queryParam("page", pageable.getPageNumber() + 1)
                .queryParam("size", pageable.getPageSize())
                .encode()
                .build()
                .toUri();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("Authorization", "KakaoAK " + key)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        return CompletableFuture.completedFuture(restTemplate.exchange(uri, HttpMethod.GET, req, Map.class).getBody());
    }
}
