package com.test.dto;


import java.util.Map;

public class Api1BlogDataMapper implements BlogDataDtoMapper{
    @Override
    public String getBlogname(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("blogname").toString();
    }

    @Override
    public String getTitle(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("title").toString();
    }

    @Override
    public String getContents(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("contents").toString();
    }

    @Override
    public String getUrl(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("url").toString();
    }

    @Override
    public String getDatetime(Object apiResponse) {
        return ((Map<String, Object>) apiResponse).get("datetime").toString();
    }
}
