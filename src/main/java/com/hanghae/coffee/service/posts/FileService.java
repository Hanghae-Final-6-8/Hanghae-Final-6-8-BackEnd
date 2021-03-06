package com.hanghae.coffee.service.posts;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.hanghae.coffee.advice.ErrorCode;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.utils.FilesUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 이미지 파일 업로드 및 컨텐츠 내용 저장
    public String uploadFile(Long uniqueId, MultipartFile multipartFile, String dirName)
        throws IOException {
        log.info("uploadFile");
        // 파일 유효성 검사
        String fileName = multipartfileToS3(uniqueId, multipartFile, dirName);

        // 시작
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private String multipartfileToS3(Long uniqueId, MultipartFile multipartFile, String dirName)
        throws IOException {
        validateFileExists(multipartFile);
        if(!multipartFile.getContentType().startsWith("image")) throw new RestException(ErrorCode.BAD_REQUEST_NOT_VAILIDATION_FILE_EXT);
        // 파일 이름 설정
        String fileName = FilesUtils.buildFileName(uniqueId, multipartFile.getOriginalFilename(),
            dirName);

        // AWS S3 직렬화
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        InputStream inputStream = multipartFile.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        amazonS3Client.putObject(
            new PutObjectRequest(bucketName, fileName, byteArrayInputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return fileName;
    }

    public String multipartStaticfileToS3(Long uniqueId, String uniqueName,
        MultipartFile multipartFile, String dirName)
        throws IOException {
        validateFileExists(multipartFile);

        // 파일 이름 설정
        String fileName = FilesUtils.buildStaticFileName(uniqueId,
            multipartFile.getOriginalFilename(), uniqueName, dirName);

        // AWS S3 직렬화
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        InputStream inputStream = multipartFile.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        amazonS3Client.putObject(
            new PutObjectRequest(bucketName, fileName, byteArrayInputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String result = URLDecoder.decode(amazonS3Client.getUrl(bucketName, fileName).toString(),
            "UTF-8");

        return result;
    }

    public String updateFile(Long uniqueId, String url, MultipartFile multipartFile, String dirName)
        throws IOException {
        // 기존 파일 삭제
        Optional<String> optionalUrl = Optional.ofNullable(url);
        optionalUrl.ifPresent(profile -> this.deleteFile(profile));

        // 파일 유효성 검사
        String fileName = multipartfileToS3(uniqueId, multipartFile, dirName);

        return amazonS3Client.getUrl(bucketName, fileName).toString();

    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new RestException(ErrorCode.NOT_FOUND_FILE);
        }
    }

    //파일 삭제
    public void deleteFile(String fileUrl) {

        String bucketUrl = amazonS3Client.getUrl(bucketName, "").toString();
        if (fileUrl.startsWith(bucketUrl)) {
            fileUrl = fileUrl.substring(bucketUrl.length());
        }
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, fileUrl);
        amazonS3Client.deleteObject(request);

    }

    //파일 존재 여부
    public String existsFile(String fileName) throws UnsupportedEncodingException {

        String getUrl = amazonS3Client.getUrl(bucketName, fileName).toString();
        if (amazonS3Client.doesObjectExist(bucketName, fileName)) {
            try {
                return URLDecoder.decode(getUrl, "UTF-8");

            } catch (UnsupportedEncodingException e) {
                throw new UnsupportedEncodingException("Object " + getUrl + " not encoding");
            }

        }

        return null;

    }

}
