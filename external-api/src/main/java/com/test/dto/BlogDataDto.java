package com.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogDataDto {
    private String blogname;
    private String title;
    private String contents;
    private String url;
    private String datetime;
}
