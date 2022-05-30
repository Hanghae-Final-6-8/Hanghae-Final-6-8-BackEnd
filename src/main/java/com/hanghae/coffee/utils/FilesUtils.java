package com.hanghae.coffee.utils;


import com.hanghae.coffee.advice.ErrorCode;
import com.hanghae.coffee.advice.RestException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.ContentDisposition;
import org.springframework.util.StringUtils;

public class FilesUtils {
    private static final String PREFIX = "/";
    private static final String SEPARATOR = "_";
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final int UNDER_BAR_INDEX = 1;
    private static final String[] PERMISSION_FILE_EXT_ARR = new String[]{"GIF", "JPEG", "JPG", "PNG"};


    public static ContentDisposition createContentDisposition(String categoryWithFileName) {
        String fileName = categoryWithFileName.substring(
            categoryWithFileName.lastIndexOf(PREFIX) + UNDER_BAR_INDEX);
        return ContentDisposition.builder("attachment")
            .filename(fileName, StandardCharsets.UTF_8)
            .build();
    }

    public static String buildFileName(Long uniqueId, String originalFileName, String dirName) throws UnsupportedEncodingException {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = Optional.of(
            originalFileName.substring(fileExtensionIndex)).orElseThrow(
            () -> new RestException(ErrorCode.BAD_REQUEST_NOT_VAILIDATION_FILE_EXT)
        );
        if(!isPermissionFileExt(fileExtension)) throw new RestException(ErrorCode.BAD_REQUEST_FILE_EXT);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());
        // 인코딩
        String.format("attachment;filename=\"%1$s\";" +
            "filename*=\"UTF-8''%1$s\";", URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));

        // 디코딩 시
        // decodeURIComponent()
        return dirName + PREFIX + uniqueId + SEPARATOR +fileName + SEPARATOR + now + fileExtension;
    }

    public static String buildStaticFileName(Long uniqueId, String originalFileName, String fileName, String dirName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        // 인코딩
//        String.format("attachment;filename=\"%1$s\";" +
//            "filename*=\"UTF-8''%1$s\";", URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));
        // 디코딩 시
        // decodeURIComponent()
        return dirName + PREFIX + uniqueId + SEPARATOR +fileName + fileExtension;
    }

    public static boolean isPermissionFileExt(String fileExtension) {

        if( !StringUtils.hasText(fileExtension) ) {
            return false;
        }

        String ext = fileExtension.toUpperCase();

        return Arrays.stream(PERMISSION_FILE_EXT_ARR).anyMatch(s -> s.contains(ext));

    }
}