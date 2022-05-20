package com.hanghae.coffee.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ContentDisposition;

public class FilesUtils {
    private static final String PREFIX = "/";
    private static final String SEPARATOR = "_";
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final int UNDER_BAR_INDEX = 1;


    public static ContentDisposition createContentDisposition(String categoryWithFileName) {
        String fileName = categoryWithFileName.substring(
                categoryWithFileName.lastIndexOf(PREFIX) + UNDER_BAR_INDEX);
        return ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
    }

    public static String buildFileName(Long uniqueId, String originalFileName, String dirName) throws UnsupportedEncodingException {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());
        // 인코딩
        String.format("attachment;filename=\"%1$s\";" +
                "filename*=\"UTF-8''%1$s\";", URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));

        // 디코딩 시
        // decodeURIComponent()
        return dirName + PREFIX + uniqueId + SEPARATOR +fileName + SEPARATOR + now + fileExtension;
    }
}
