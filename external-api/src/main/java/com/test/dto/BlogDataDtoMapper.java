package com.test.dto;

import java.time.LocalDateTime;

public interface BlogDataDtoMapper {
    String getBlogname(Object apiResponse);
    String getTitle(Object apiResponse);
    String getContents(Object apiResponse);
    String getUrl(Object apiResponse);
    String getDatetime(Object apiResponse);
}
