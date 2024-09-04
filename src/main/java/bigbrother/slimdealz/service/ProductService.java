package bigbrother.slimdealz.service;

import bigbrother.slimdealz.dto.product.ProductConverter;
import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final S3Service s3Service;

    // 상품 검색
    public List<ProductDto> searchProducts(String keyword, Long lastSeenId, int size) {
        List<ProductDto> products = productRepository.searchByKeyword(keyword, lastSeenId, size);

        products.forEach(product -> {
            String imageUrl = s3Service.getProductImageUrl(product.getName());
        product.setImageUrl(imageUrl);
        });

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 오늘의 최저가 상품 목록
    public List<ProductDto> findLowestPriceProducts() {
        List<ProductDto> products = productRepository.findLowestPriceProducts();

        products.forEach(product -> {
            String imageUrl = s3Service.getProductImageUrl(product.getName());
            product.setImageUrl(imageUrl);
        });

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 상품 상세 페이지 정보
    public ProductDto getProductWithLowestPriceByName(String productName) {
        ProductDto product = productRepository.findProductWithLowestPriceByName(productName);

        if(product == null) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }

        String imageUrl = s3Service.getProductImageUrl(productName);
        product.setImageUrl(imageUrl);

        return product;
    }

    // 카테고리 별 상품 조회
    public List<ProductDto> findByCategory(String category, Long lastSeenId, int size) {
        List<ProductDto> products = productRepository.findByCategory(category, lastSeenId, size);

        products.forEach(product -> {
            String imageUrl = s3Service.getProductImageUrl(product.getName());
            product.setImageUrl(imageUrl);
        });

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }

    // 판매처 리스트
    public List<ProductDto> getProductWithVendors(String productName) {
        List<ProductDto> products = productRepository.findProductWithVendors(productName);

        products.forEach(product -> {
            String imageUrl = s3Service.getProductImageUrl(product.getName());
            product.setImageUrl(imageUrl);
        });

        return products;
    }

    // 랜덤 추천
    public  List<ProductDto> findRandomProducts() {
        List<ProductDto> products = productRepository.findRandomProducts();

        products.forEach(product -> {
            String imageUrl = s3Service.getProductImageUrl(product.getName());
            product.setImageUrl(imageUrl);
        });

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
    }
}