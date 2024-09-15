package bigbrother.slimdealz.controller;

import bigbrother.slimdealz.dto.product.ChartDto;
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
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    @GetMapping("/search")
    public List<ProductDto> searchProducts(@RequestParam("keyword") String keyword,
                                           @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<List<ProductDto>> future = CompletableFuture.supplyAsync(() -> {
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
        }, executor);

        try {
            return future.get(10, TimeUnit.SECONDS);  // 10초 타임아웃 설정
        } catch (TimeoutException e) {
            log.error("Search request timed out");
            throw new CustomException(CustomErrorCode.SEARCH_TIMEOUT);  // 타임아웃에 대한 커스텀 에러
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.SEARCH_NO_RESULT);
        } finally {
            executor.shutdown();
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

    @GetMapping("/price-compare")
    public List<ChartDto> getChartData(
            @RequestParam("productName") String productName,
            @RequestParam("dateLimit") String dateLimit
    ){
        try{
            List<ChartDto> chart = productService.getChartData(productName, dateLimit);
            return chart;
        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }
}