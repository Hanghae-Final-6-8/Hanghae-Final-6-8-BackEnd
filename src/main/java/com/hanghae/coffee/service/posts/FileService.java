package com.hanghae.coffee.service.posts;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.utils.FilesUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
public class FileService extends Posts {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 이미지 파일 업로드 및 컨텐츠 내용 저장
    public String uploadFile(Long uniqueId, MultipartFile multipartFile, String dirName)
        throws IOException {
        log.info("uploadFile");
        // 파일 유효성 검사
        String fileName = multipartfileToS3(uniqueId, multipartFile, dirName);

//        // 로컬 저장
//        File storedFile;
//        String storedFileName;
//
//        String filePath = "C:\\sparta\\image\\";
//
//        do{
//            storedFile = new File(filePath + fileName);
//        }while(storedFile.exists());
//
//        storedFile.getParentFile().mkdirs();
//        multipartFile.transferTo(storedFile);
//
//        PostsImage postsImage = new PostsImage(posts,filePath + fileName);

        // 시작

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private String multipartfileToS3(Long uniqueId, MultipartFile multipartFile, String dirName)
        throws IOException {
        validateFileExists(multipartFile);

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

    public String updateFile(Long uniqueId, String url, MultipartFile multipartFile, String dirName)
        throws IOException {
        // 기존 파일 삭제
        deleteFile(url);

        // 파일 유효성 검사
        String fileName = multipartfileToS3(uniqueId, multipartFile, dirName);


        return amazonS3Client.getUrl(bucketName, fileName).toString();

    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new NullPointerException();
        }
    }

    //파일 삭제
    public void deleteFile(String fileUrl) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, fileUrl);
        amazonS3Client.deleteObject(request);

    }

}