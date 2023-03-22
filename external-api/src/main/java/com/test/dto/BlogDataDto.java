package com.test.dto;

import lombok.*;

@Getter @Setter
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
