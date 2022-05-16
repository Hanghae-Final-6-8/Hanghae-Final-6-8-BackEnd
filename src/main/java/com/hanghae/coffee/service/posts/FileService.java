package com.hanghae.coffee.service.posts;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.hanghae.coffee.model.Posts;


import com.hanghae.coffee.model.PostsImage;
import com.hanghae.coffee.repository.posts.PostsImageRepository;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.utils.FilesUtils;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FileService extends Posts {

    private final AmazonS3Client amazonS3Client;
    private final PostsImageRepository postsImageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 이미지 파일 업로드 및 컨텐츠 내용 저장
    public String uploadFile(Posts posts, MultipartFile multipartFile) throws IOException {
        log.info("uploadFile");
        // 파일 유효성 검사
        validateFileExists(multipartFile);

        // 파일 이름 설정
        String fileName = FilesUtils.buildFileName(posts, multipartFile.getOriginalFilename());

        // AWS S3 직렬화
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        InputStream inputStream = multipartFile.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayInputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
                PostsImage postsImage = new PostsImage(posts,amazonS3Client.getUrl(bucketName, fileName).toString());

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
        postsImageRepository.save(postsImage);

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    @Transactional
    public String updateFile(Posts posts, MultipartFile multipartFile) throws IOException {
        PostsImage postsImage = postsImageRepository.findByPosts_Id(posts.getId());
        // 기존 파일 삭제
        deleteFile(posts.getId());
        postsImageRepository.deleteById(posts.getId());

        // 파일 유효성 검사
        validateFileExists(multipartFile);

        // 파일 이름 설정
        String fileName = FilesUtils.buildFileName(posts, multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        InputStream inputStream = multipartFile.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);
        objectMetadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, byteArrayInputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        postsImage = new PostsImage(posts,amazonS3Client.getUrl(bucketName, fileName).toString());
        postsImageRepository.save(postsImage);


        return amazonS3Client.getUrl(bucketName, fileName).toString();

    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new NullPointerException();
        }
    }

    //파일 삭제
    public void deleteFile(Long post_id){
        String fileName = postsImageRepository.findByPosts_Id(post_id).getImageUrl();
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName, fileName);
        amazonS3Client.deleteObject(request);

    }

}
