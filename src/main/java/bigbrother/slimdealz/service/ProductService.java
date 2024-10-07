package bigbrother.slimdealz.service;

import bigbrother.slimdealz.dto.product.ChartDto;
import bigbrother.slimdealz.dto.product.ProductConverter;
import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.dto.product.ReviewDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.repository.Product.PriceHistoryRepository;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import bigbrother.slimdealz.repository.Product.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final S3Service s3Service;
    private final PriceHistoryRepository priceHistoryRepository;
    private final ReviewRepository reviewRepository;

    // 상품 검색
    @Transactional
    public List<ProductDto> searchProducts(String keyword, Long lastSeenId, String lastSeenProductName, int size) {
        List<ProductDto> products = productRepository.searchByKeyword(keyword, lastSeenId, lastSeenProductName, size)
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getProductName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                }) //converter 를 통해 DTO 로 변환
                .collect(Collectors.toList()); // stream의 변환된 요소들을 리스트로 반환

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 오늘의 최저가 상품 목록
    @Transactional
    public List<ProductDto> findLowestPriceProducts() {
        List<ProductDto> products = productRepository.findLowestPriceProducts()
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getProductName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                })
                .collect(Collectors.toList());

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 상품 상세 페이지 정보
    @Transactional
    public ProductDto getProductWithLowestPriceByName(String productName) {
        Product product = productRepository.findProductWithLowestPriceByName(productName);

        if(product == null) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }

        ProductDto productDto = ProductConverter.toProductDTO(product);

        String imageUrl = s3Service.getProductImageUrl(productName);
        productDto.setImageUrl(imageUrl);

        return productDto;
    }

    // 상품 조회수 증가
    @Transactional
    public void incrementViewCount(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND));

        product.incrementViewCount();
        productRepository.save(product);
    }

    // 카테고리 별 상품 조회
    @Transactional
    public List<ProductDto> findByCategory(String category, Long lastSeenId, String lastSeenProductName, int size) {
        List<ProductDto> products = productRepository.findByCategory(category, lastSeenId, lastSeenProductName, size)
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getProductName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                })
                .collect(Collectors.toList());

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 판매처 리스트
    @Transactional
    public List<ProductDto> getProductWithVendors(String productName) {
        List<Product> products = productRepository.findProductWithVendors(productName);

        return products.stream()
                .map(ProductConverter::toProductDTO)
                .collect(Collectors.toList());
    }

    // 랜덤 추천
    @Transactional
    public  List<ProductDto> findRandomProducts() {
        List<ProductDto> products = productRepository.findRandomProducts()
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getProductName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                })
                .collect(Collectors.toList());

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 가격 비교 차트
    @Transactional
    public List<ChartDto> getChartData(String productName, String dateLimit) {
        LocalDateTime startDateTime;

        // dateLimit 값에 따른 날짜 계산
        if ("week".equalsIgnoreCase(dateLimit)) {
            startDateTime = LocalDateTime.now().minusWeeks(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else if ("month".equalsIgnoreCase(dateLimit)) {
            startDateTime = LocalDateTime.now().minusMonths(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else {
            throw new IllegalArgumentException("Invalid dateLimit. It should be either 'week' or 'month'.");
        }

        // LocalDateTime을 바로 사용하여 데이터 조회
        return priceHistoryRepository.findChartData(productName, startDateTime);
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void updatePopularProducts() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        List<ProductDto> popularProducts = productRepository.findPopularProducts(oneHourAgo)
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getProductName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                })
                .collect(Collectors.toList());

        if (popularProducts.isEmpty()) {
            popularProducts = productRepository.findTopProductsByPrice()
                    .stream()
                    .map(product -> {
                        ProductDto productDto = ProductConverter.toProductDTO(product);
                        String imageUrl = s3Service.getProductImageUrl(product.getProductName());
                        productDto.setImageUrl(imageUrl);
                        return productDto;
                    })
                    .toList();
        }

        // 점수 업데이트
        for(ProductDto p : popularProducts) {
            Product product = productRepository.findById(p.getId())
                    .orElseThrow(() -> new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND));

            int delta = popularProducts.stream().anyMatch(productDto -> productDto.getId().equals(product.getId())) ? -1 : 1;
            product.adjustScore(delta);
            productRepository.save(product);
        }

        System.out.println("인기 급상승 상품이 업데이트 되었습니다.");
    }

    @Transactional
    public List<ProductDto> getPopularProducts() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        List<ProductDto> popularProducts = productRepository.findPopularProducts(oneHourAgo)
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getProductName());
                    productDto.setImageUrl(imageUrl);
                    return productDto;
                })
                .collect(Collectors.toList());

        if (popularProducts.isEmpty()) {
            popularProducts = productRepository.findTopProductsByPrice()
                    .stream()
                    .map(product -> {
                        ProductDto productDto = ProductConverter.toProductDTO(product);
                        String imageUrl = s3Service.getProductImageUrl(product.getProductName());
                        productDto.setImageUrl(imageUrl);
                        return productDto;
                    })
                    .collect(Collectors.toList());
        }

        return popularProducts;
    }

    public List<ReviewDto> getReview(String productName) {

        List<ReviewDto> reviewDtosByProductName = reviewRepository.findReviewDtosByProductName(productName);

        if(reviewDtosByProductName.isEmpty()) {
            throw new CustomException(CustomErrorCode.REVIEW_NOT_FOUND);
        }
        return reviewDtosByProductName;
    }


}


