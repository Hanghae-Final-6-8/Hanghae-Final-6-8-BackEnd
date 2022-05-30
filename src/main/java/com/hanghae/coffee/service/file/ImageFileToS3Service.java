package com.hanghae.coffee.service.file;

import com.hanghae.coffee.service.posts.FileService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageFileToS3Service {

    private final FileService fileService;


    public String beansImageSave(Long id,String fileName) throws IOException {

        String url = "beans/images/"+id+"_"+fileName+".png";
        Optional<String> existsFileName = Optional.ofNullable(fileService.existsFile(url));

        if(existsFileName.isPresent()){
            return existsFileName.get();
        }

        String uid = Long.toString(id);
        File file = new File("C:\\Users\\82104\\Pictures\\beans\\" + uid+".png");
        Boolean res = this.checkPath(file);

        if (!res) {
            return null;
        }

        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false,
            file.getName(), (int) file.length(), file.getParentFile());

        try {
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);

        } catch (IOException ex) {
            // do something.
        }

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        url = fileService.multipartStaticfileToS3(id, fileName, multipartFile, "beans/images");

        log.info(url);

        return url;
    }

    public String cafeLogoImageSave(Long id,String fileName) throws IOException {
        String url = "cafe/logo/images/"+id+"_"+fileName+".jpg";
        Optional<String> existsFileName = Optional.ofNullable(fileService.existsFile(url));

        if(existsFileName.isPresent()){
            return existsFileName.get();
        }

        String uid = Long.toString(id);
        File file = new File("C:\\Users\\82104\\Pictures\\cafelogo\\" + uid+".jpg");
        Boolean res = this.checkPath(file);

        if (!res) {
            return null;
        }

        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false,
            file.getName(), (int) file.length(), file.getParentFile());

        try {
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);

        } catch (IOException ex) {
            // do something.
        }

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        url = fileService.multipartStaticfileToS3(id, fileName, multipartFile, "cafe/logo/images");

        log.info(url);

        return url;
    }

    public String cafeBackGroundImageSave(Long id,String fileName) throws IOException {

        String url = "cafe/background/images/"+id+"_"+fileName+".jpg";
        Optional<String> existsFileName = Optional.ofNullable(fileService.existsFile(url));

        if(existsFileName.isPresent()){
            return existsFileName.get();
        }

        String uid = Long.toString(id);
        File file = new File("C:\\Users\\82104\\Pictures\\cafebackground\\" + uid+".jpg");
        Boolean res = this.checkPath(file);
        log.info("id :: "+ uid+ " res :: " +String.valueOf(res));
        if (!res) {
            return null;
        }

        FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false,
            file.getName(), (int) file.length(), file.getParentFile());

        try {
            InputStream input = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            IOUtils.copy(input, os);

        } catch (IOException ex) {
            // do something.
        }

        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

        url = fileService.multipartStaticfileToS3(id, fileName, multipartFile, "cafe/background/images");

        log.info(url);

        return url;
    }


    private Boolean checkPath(File file) {

        return file.exists();
    }

}
