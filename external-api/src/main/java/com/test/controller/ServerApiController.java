package com.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.dto.*;
import com.test.search.error.CustomExceptionHandler;
import com.test.search.service.KeywordsService;
import com.test.service.Api1Service;
import com.test.service.Api2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.test.search.error.Constants.ExceptionClass.SEARCH;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class ServerApiController {
    private final CacheManager cacheManager;
    private final KeywordsService keywordsService;
    private final Api1Service api1Service;
    private final Api2Service api2Service;

    @GetMapping("/list")
    public CompletableFuture<Page<BlogDataDto>> fetchApiData(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "sort", required = false, defaultValue = "acc") String sort,
            Pageable pageable
    ) throws CustomExceptionHandler {
        validateQuery(query);

        /**
         * 인기 검색어 카운트
         * - common-api module
         */
        keywordsService.countUpForKeyword(query.trim().toLowerCase());

        String cacheKey = generateCacheKey(query, sort, pageable);
//        Cache.ValueWrapper cachedValue = cacheManager.getCache("fetchApiDataCache").get(cacheKey);
//        if (cachedValue != null) {
//            return CompletableFuture.completedFuture((Page<BlogDataDto>) cachedValue.get());
//        }

        String sortConvert;
        CompletableFuture<Page<BlogDataDto>> response;
        try {
            sortConvert = "acc".equals(sort) ? "accuracy" : "recency";
            response = fetchApi1Data(query, sortConvert, pageable);
        } catch (CustomExceptionHandler e) {
            sortConvert = "acc".equals(sort) ? "sim" : "date";
            response = fetchApi2Data(query, sortConvert, pageable);
        }

        return response.thenApply(page -> {
            cacheManager.getCache("fetchApiDataCache").put(cacheKey, page);
            return page;
        });
    }

    private String generateCacheKey(String query, String sort, Pageable pageable) {
        return String.format("query:%s,sort:%s,pageable:%s", query, sort, pageable);
    }

    public CompletableFuture<Page<BlogDataDto>> fetchApi1Data(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "sort", required = false, defaultValue = "accuracy") String sort,
            Pageable pageable
    ) throws CustomExceptionHandler {
        /**
         * 카카오 검색 api
         */
        CompletableFuture<Map> response = api1Service.fetchApi1Data(query, sort, pageable);

        Map<String, String> metaMap;
        try {
            metaMap = (Map) response.get().get("meta");
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomExceptionHandler(SEARCH, INTERNAL_SERVER_ERROR, "통신 에러. 잠시 후 다시 실행해 주세요.");
        }
        long count = Long.valueOf(String.valueOf(metaMap.get("total_count")));

        List documents = getDocuments(response);
        List<BlogDataDto> blogDataDtoList = mapDocumentsToDtoList(documents, new Api1BlogDataMapper());

        return CompletableFuture.completedFuture(new PageImpl<>(blogDataDtoList, pageable, count));
    }

    public CompletableFuture<Page<BlogDataDto>> fetchApi2Data(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "sort", required = false, defaultValue = "sim") String sort,
            Pageable pageable
    ) throws CustomExceptionHandler {
        /**
         * 네이버 검색 api
         */
        CompletableFuture<Map> response = api2Service.fetchApi2Data(query, sort, pageable);

        long count = 0;
        try {
            count = Long.parseLong(response.get().get("total").toString());
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomExceptionHandler(SEARCH, INTERNAL_SERVER_ERROR, "통신 에러. 잠시 후 다시 실행해 주세요.");
        }

        List documents = getDocuments(response);
        List<BlogDataDto> blogDataDtoList = mapDocumentsToDtoList(documents, new Api2BlogDataMapper());

        return CompletableFuture.completedFuture(new PageImpl<>(blogDataDtoList, pageable, count));
    }

    private void validateQuery(String query) throws CustomExceptionHandler {
        if (query.isEmpty()) {
            throw new CustomExceptionHandler(SEARCH, NOT_IMPLEMENTED, "검색어를 입력해주세요.");
        }
    }

    private List getDocuments(CompletableFuture<Map> response) throws CustomExceptionHandler {
        try {
            return (List) response.get().get("documents");
        } catch (InterruptedException | ExecutionException e) {
            throw new CustomExceptionHandler(SEARCH, INTERNAL_SERVER_ERROR, "통신 에러. 잠시 후 다시 실행해 주세요.");
        }
    }

    private List<BlogDataDto> mapDocumentsToDtoList(List<?> documents, BlogDataDtoMapper mapper) {
        List<BlogDataDto> blogDataDtoList = new ArrayList<>();
        for (Object obj : documents) {
            BlogDataDto blogDataDto = BlogDataDto.builder()
                    .blogname(mapper.getBlogname(obj))
                    .title(mapper.getTitle(obj))
                    .contents(mapper.getContents(obj))
                    .url(mapper.getUrl(obj))
                    .datetime(mapper.getDatetime(obj))
                    .build();
            blogDataDtoList.add(blogDataDto);
        }
        return blogDataDtoList;
    }

}
