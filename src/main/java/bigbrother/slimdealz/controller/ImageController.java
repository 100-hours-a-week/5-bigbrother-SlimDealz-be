package bigbrother.slimdealz.controller;

import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/image")
public class ImageController {

    private final S3Service s3Service;

    @GetMapping
    public ResponseEntity<byte[]> getProductImage(@RequestParam String productName) throws IOException {
        try {
            String fileName = s3Service.findImageName(productName);

            byte[] imageData = s3Service.downloadImage(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(imageData);
        }
        catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        }
        catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
