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
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 상품명과 일치하는 이미지 파일 찾기
    public String findImageName(String productName) {
        ObjectListing objectListing = amazonS3.listObjects(bucket);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

        return objectSummaries.stream()
                .map(S3ObjectSummary::getKey)
                .filter(key -> key.contains(productName))
                .findFirst()
                .orElseThrow(() -> new CustomException(CustomErrorCode.PRODUCT_IMAGE_NOT_FOUND));
    }

    // 이미지 파일 URL 반환
    public String getImageUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public String getProductImageUrl (String productName) {
        String fileName = findImageName(productName);

        return getImageUrl(fileName);
    }
}
