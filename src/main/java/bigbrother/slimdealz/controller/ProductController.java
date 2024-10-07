package bigbrother.slimdealz.controller;

import bigbrother.slimdealz.dto.product.ChartDto;
import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.dto.product.ReviewDto;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.service.ProductService;
import bigbrother.slimdealz.service.S3Service;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
                                           @RequestParam(value = "lastSeenProductName", required = false) String lastSeenProductName,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<List<ProductDto>> future = CompletableFuture.supplyAsync(() -> {
            try {
                List<ProductDto> products = productService.searchProducts(keyword, lastSeenId, lastSeenProductName ,size);
                products.forEach(product -> {
                    String imageUrl = s3Service.getProductImageUrl(product.getProductName());
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
        try {
            List<ProductDto> products = productService.findLowestPriceProducts();

            products.forEach(product -> {
                String imageUrl = s3Service.getProductImageUrl(product.getProductName());
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

    @GetMapping("/product-detail")
    public ProductDto getProductWithLowestPriceByName(@RequestParam("productName") String productName,
                                                      @RequestParam("productId") Long productId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        try {
            ProductDto productDto = productService.getProductWithLowestPriceByName(productName);

            Cookie[] cookies = request.getCookies();
            Cookie oldCookie = findCookie(cookies, "view_count");

            boolean isNewViewCount = true;

            if(oldCookie != null) {
                if(oldCookie.getValue().contains("[" + productId + "]")) {
                    isNewViewCount = false;
                    // 조회수 증가
//                    productService.incrementViewCount(productId);
                }
                else {
                    oldCookie.setValue(oldCookie.getValue() + "[" + productId + "]");
                    oldCookie.setPath("/");
                    response.addCookie(oldCookie);
                }
            }
            else {
                Cookie newCookie = new Cookie("view_count", "[" + productId + "]");
                newCookie.setPath("/");
                response.addCookie(newCookie);
//                productService.incrementViewCount(productId);
            }

            if(isNewViewCount) {
                productService.incrementViewCount(productId);
            }

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

    private Cookie findCookie(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(name)) {
                    return c;
                }
            }
        }
        return null;
    }

    @GetMapping("/products")
    public List<ProductDto> findByCategory(@RequestParam("category") String category,
                                           @RequestParam(value = "lastSeenId", required = false) Long lastSeenId,
                                           @RequestParam(value = "lastSeenProductName", required = false) String lastSeenProductName,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<ProductDto> products = productService.findByCategory(category, lastSeenId, lastSeenProductName, size);

            products.forEach(product -> {
                String imageUrl = s3Service.getProductImageUrl(product.getProductName());
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
                String imageUrl = s3Service.getProductImageUrl(product.getProductName());
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

    @GetMapping("/popular-products")
    public List<ProductDto> findPopularProducts() {
        try {

            List<ProductDto> products = productService.getPopularProducts();

            products.forEach(product -> {
                String imageUrl = s3Service.getProductImageUrl(product.getProductName());
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
    ) {
        try {
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

    @GetMapping("/review")
    public List<ReviewDto> getReviews(@RequestParam("productName") String productName) {
        try {
            List<ReviewDto> reviews = productService.getReview(productName);
            return reviews;
        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.REVIEW_NOT_FOUND);
        }
    }

}