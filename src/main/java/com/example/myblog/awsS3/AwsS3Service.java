package com.example.myblog.awsS3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.region.static}")
    private String region;

    private final AmazonS3 amazonS3Client;

    public List<String> uploadFile(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + file.getOriginalFilename();
            String fileUrl = S3FileUpload(file,fileName);
            fileNames.add(fileUrl);
        }

        return fileNames;
    }

    public String S3FileUpload(MultipartFile file, String fileName) {
        String fileUrl = "https://" + bucket +".s3."+ region + ".amazonaws.com/" + fileName;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
            return fileUrl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("이미지의 크기가 너무 큽니다");
        }
    }

    public void deleteFile(List<String> postImage) throws IOException {
        try {
            for (String url : postImage) {
                String fileName = url.substring(url.lastIndexOf("/") + 1);
                amazonS3Client.deleteObject(bucket, fileName);
            }
        } catch (SdkClientException e) {
            throw new IOException("S3에서 이미지를 삭제하는 도중 에러가 발생!", e);
        }
    }


}
