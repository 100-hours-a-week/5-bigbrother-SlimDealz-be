package bigbrother.slimdealz.service;

import bigbrother.slimdealz.config.S3Config;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {
    private AmazonS3 amazonS3;
    private S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public byte[] downloadImage(String fileName) throws IOException {
        S3Object s3Object = amazonS3.getObject(bucket, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return outputStream.toByteArray();
    }

    // 상품명과 동일한 이름을 가진 이미지 파일 탐색
    public String findImageName(String productName) {
        ObjectListing objectListing = amazonS3.listObjects(bucket);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

        return objectSummaries.stream()
                .map(S3ObjectSummary::getKey)
                .filter(key -> key.contains(productName))
                .findFirst()
                .orElseThrow(() -> new CustomException( CustomErrorCode.PRODUCT_IMAGE_NOT_FOUND));
    }


}
