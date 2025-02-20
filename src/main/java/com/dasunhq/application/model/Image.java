package com.dasunhq.application.model;

import java.sql.Blob;

public class Image {
    private Long id;
    private String fileName;
    private String filePath;
    private Blob image;
    private String fileType;
    private String downloadUrl;

    @Many
    private Product product;
}
