package com.dasunhq.application.dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long id;
    private String imageName;
    private String downloadUrl;
}
