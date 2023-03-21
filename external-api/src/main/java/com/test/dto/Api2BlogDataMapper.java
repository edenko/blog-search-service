package com.test.dto;

import java.util.Map;

public class Api2BlogDataMapper implements BlogDataDtoMapper{
    @Override
    public String getBlogname(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("bloggername").toString();
    }

    @Override
    public String getTitle(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("title").toString();
    }

    @Override
    public String getContents(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("description").toString();
    }

    @Override
    public String getUrl(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("link").toString();
    }

    @Override
    public String getDatetime(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("postdate").toString();
    }
}
