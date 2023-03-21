package com.test.controller;

import com.test.dto.BlogDataDto;
import com.test.search.error.CustomExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

import static com.test.search.error.Constants.ExceptionClass.SEARCH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@SpringBootTest
class ServerApiControllerTest {

    @Autowired
    private ServerApiController serverApiController;

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
}