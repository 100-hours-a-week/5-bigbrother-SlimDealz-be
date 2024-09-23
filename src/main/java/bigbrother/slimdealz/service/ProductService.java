package bigbrother.slimdealz.service;

import bigbrother.slimdealz.dto.product.ChartDto;
import bigbrother.slimdealz.dto.product.ProductConverter;
import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.repository.Product.PriceHistoryRepository;
import bigbrother.slimdealz.repository.Product.ProductRepository;
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

    // 상품 검색
    @Transactional
    public List<ProductDto> searchProducts(String keyword, Long lastSeenId, int size) {
        List<ProductDto> products = productRepository.searchByKeyword(keyword, lastSeenId, size)
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
    public List<ProductDto> findByCategory(String category, Long lastSeenId, int size) {
        List<ProductDto> products = productRepository.findByCategory(category, lastSeenId, size)
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
}