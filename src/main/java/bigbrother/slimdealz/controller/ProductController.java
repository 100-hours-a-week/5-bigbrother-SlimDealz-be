package bigbrother.slimdealz.controller;

import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.service.ProductService;
import bigbrother.slimdealz.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    @GetMapping("/search")
    public List<ProductDto> searchProducts(@RequestParam("keyword") String keyword,
                                           @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<ProductDto> products = productService.searchProducts(keyword, lastSeenId, size);

            products.forEach(product -> {
                String imageUrl = s3Service.getProductImageUrl(product.getName());
                product.setImageUrl(imageUrl);
            });
            return products;

        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.SEARCH_NO_RESULT);
        }
    }

    @GetMapping("/today-lowest-products")
    public List<ProductDto> findLowestPriceProducts() {
        try{
            List<ProductDto> products = productService.findLowestPriceProducts();

            products.forEach(product -> {
                String imageUrl = s3Service.getProductImageUrl(product.getName());
                product.setImageUrl(imageUrl);
            });
            return products;
        }
        catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @GetMapping("/product-detail")
    public ProductDto getProductWithLowestPriceByName(@RequestParam("productName") String productName) {
        try {
            ProductDto productDto = productService.getProductWithLowestPriceByName(productName);

            String imageUrl = s3Service.getProductImageUrl(productName);
            productDto.setImageUrl(imageUrl);

            return productDto;

        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @GetMapping("/products")
    public List<ProductDto> findByCategory(@RequestParam("category") String category,
                                           @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<ProductDto> products = productService.findByCategory(category, lastSeenId, size);

            products.forEach(product -> {
                String imageUrl = s3Service.getProductImageUrl(product.getName());
                product.setImageUrl(imageUrl);
            });
            return products;

        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @GetMapping("/vendor-list")
    public List<ProductDto> getProductWithVendors(@RequestParam("productName") String productName) {
        try {
            return productService.getProductWithVendors(productName);
        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_URL_NOT_FOUND);
        }
    }

    // 랜덤 추천
    @GetMapping("/random-products")
    public List<ProductDto> findRandomProducts() {
        try {
            List<ProductDto> products = productService.findRandomProducts();
            products.forEach(product -> {
                String imageUrl = s3Service.getProductImageUrl(product.getName());
                product.setImageUrl(imageUrl);
            });
            return products;

        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }

}

