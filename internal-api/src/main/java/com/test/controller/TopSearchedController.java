package com.test.controller;

import com.test.dto.TopSearchedRespDto;
import com.test.service.TopSearchedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/top")
public class TopSearchedController {

    public final TopSearchedService topSearchedService;

    @GetMapping("/list")
    public List<TopSearchedRespDto> fetchTopSearchedList() {
        return topSearchedService.fetchTopSearchedList();
    }
}
