package com.ssafy.homescout.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadImage(MultipartFile image) {
        try {
            if(getFileExtension(image)) {
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename(); // 고유한 파일 이름 생성

                // 메타데이터 설정
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(image.getContentType());
                metadata.setContentLength(image.getSize());

                // S3에 파일 업로드 요청 생성
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, image.getInputStream(), metadata);

                // S3에 파일 업로드
                amazonS3.putObject(putObjectRequest);

                return amazonS3.getUrl(bucket, fileName).toString();
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패했습니다.");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지 업로드에 실패했습니다.");
    }

    private boolean getFileExtension(MultipartFile file){
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        return "jpg".equals(extension) || "png".equals(extension) || "jpeg".equals(extension);
    }

}
