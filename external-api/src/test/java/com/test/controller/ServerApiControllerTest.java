package com.test.controller;

import com.test.dto.BlogDataDto;
import com.test.search.error.CustomExceptionHandler;
import com.test.service.Api1Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

import static com.test.search.error.Constants.ExceptionClass.SEARCH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ServerApiControllerTest {

    @Autowired
    private ServerApiController serverApiController;
    @InjectMocks
    private ServerApiController serverApiControllerMock;
    @InjectMocks
    private Api1Service api1Service;
    @Mock
    private CacheManager cacheManager;

    @Test
    @DisplayName("api1 통신 에러 발생, api2 호출 테스트")
    public void testFetchApiData_withNetworkErrorInApi1_callsApi2() throws CustomExceptionHandler {
        // give
        String testQuery = "test query";
        String sort = "acc";
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.unsorted());
        CompletableFuture<Page<BlogDataDto>> actualResultFuture = serverApiController.fetchApiData(testQuery, sort, pageRequest);

        ServerApiController controllerSpy = spy(serverApiController);

        doThrow(new CustomExceptionHandler(SEARCH, INTERNAL_SERVER_ERROR, "통신 에러. 잠시 후 다시 실행해 주세요."))
                .when(controllerSpy).fetchApi1Data(eq(testQuery), anyString(), eq(pageRequest));

        doReturn(actualResultFuture)
                .when(controllerSpy).fetchApi2Data(eq(testQuery), anyString(), eq(pageRequest));

        // when
        CompletableFuture<Page<BlogDataDto>> responseFuture = controllerSpy.fetchApiData(testQuery, sort, pageRequest);
        Page<BlogDataDto> response = responseFuture.join();

        // then
        assertEquals(actualResultFuture.join(), response);
        verify(controllerSpy, times(1)).fetchApi1Data(testQuery, "accuracy", pageRequest);
        verify(controllerSpy, times(1)).fetchApi2Data(testQuery, "sim", pageRequest);
    }

    @Test
    @DisplayName("100개 동시성 테스트")
    public void testFetchApiDataConcurrency() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        int numRequests = 100;
        String query = "test";
        String sort = "accuracy";
        Pageable pageable = PageRequest.of(0, 10);

        List<CompletableFuture<Page<BlogDataDto>>> futures = new ArrayList<>();

        for (int i = 0; i < numRequests; i++) {
            CompletableFuture<Page<BlogDataDto>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return serverApiController.fetchApiData(query, sort, pageable).join();
                } catch (CustomExceptionHandler e) {
                    e.printStackTrace();
                    return null;
                }
            }, executorService);
            futures.add(future);
        }

        for (CompletableFuture<Page<BlogDataDto>> future : futures) {
            Page<BlogDataDto> result = future.get();
            assertNotNull(result);
        }

        executorService.shutdown();
    }

    @Test
    @DisplayName("Redis 캐시 테스트 - 구현 중")
    public void testCachingBehavior() throws Exception {
        // give
        List<BlogDataDto> blogDataDtoList = new ArrayList<>();
        BlogDataDto dto = new BlogDataDto();
        dto.setTitle("Test Title");
        dto.setContents("Test Contents");
        blogDataDtoList.add(dto);
        Page<BlogDataDto> page = new PageImpl<>(blogDataDtoList, PageRequest.of(0, 10), 1);

        // when
//        Cache cache = mock(Cache.class);
//        when(cacheManager.getCache("fetchApiDataCache")).thenReturn(cache);
//        CompletableFuture<Page<BlogDataDto>> response = serverApiControllerMock.fetchApiData("test", "acc", PageRequest.of(0, 10));

        // then
//        verify(serverApiControllerMock, times(1)).fetchApi1Data(any(), any(), any());
    }

}