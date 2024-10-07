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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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


        if(productName == null || productName.isEmpty()) {
            throw new CustomException(CustomErrorCode.INVALID_PRODUCT_NAME);
        }

        ObjectListing objectListing = amazonS3.listObjects(bucket);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();

        return objectSummaries.stream()
                .map(S3ObjectSummary::getKey)
                .filter(key -> key.contains(productName))
                .findFirst()
                .orElseThrow(() -> new CustomException(
                        CustomErrorCode.PRODUCT_IMAGE_NOT_FOUND,
                        "Product image not found for: " + productName
                ));

    }

    // 이미지 파일 URL 반환
    public String getImageUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public String getProductImageUrl (String productName) {
        //원복 필요시 주석처리된 코드들을 해제하고, 아래의 try-catch구문을 주석처리할 것
//        String fileName = findImageName(productName);

        try {
            // productName만 UTF-8로 인코딩
            String reProductName = URLEncoder.encode(productName, "UTF-8");
            // 인코딩된 productName을 포함한 URL 반환
            return "https://s3.ap-northeast-2.amazonaws.com/ktbbigbrother3/img/" + reProductName + ".jpg";
        } catch (UnsupportedEncodingException e) {
            // 인코딩 오류 처리9
            e.printStackTrace();
            return null;
        }
//        return getImageUrl(fileName);
    }
}