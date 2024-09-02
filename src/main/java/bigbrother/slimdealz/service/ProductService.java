package bigbrother.slimdealz.service;

import bigbrother.slimdealz.dto.product.ProductConverter;
import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.entity.product.Vendor;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import bigbrother.slimdealz.repository.Product.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static bigbrother.slimdealz.entity.product.QVendor.vendor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final S3Service s3Service;
  
    private final VendorRepository vendorRepository;

    // 필드 값 error인 경우, 예외처리
    private void validateVendorUrl(Product product) {
        if("error".equalsIgnoreCase(product.getVendorUrl())) {
            throw new CustomException(CustomErrorCode.PRODUCT_URL_INVALID);
        }
    }

    private void validateUrl(Vendor vendor) {
        if ("error".equalsIgnoreCase(vendor.getVendorUrl())) {
            throw new CustomException(CustomErrorCode.PRODUCT_URL_INVALID);
        }
    }

    // 상품 검색
    public List<ProductDto> searchProducts(String keyword, Long lastSeenId, int size) {
        List<ProductDto> products = productRepository.searchByKeyword(keyword, lastSeenId, size)
                .stream()
                .map(product -> {
                            ProductDto productDto = ProductConverter.toProductDTO(product);
                            String imageUrl = s3Service.getProductImageUrl(product.getName());
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
    public List<ProductDto> findLowestPriceProducts() {
        List<ProductDto> products = productRepository.findLowestPriceProducts()
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getName());
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

    // 카테고리 별 상품 조회
    public List<ProductDto> findByCategory(String category, Long lastSeenId, int size) {
        List<ProductDto> products = productRepository.findByCategory(category, lastSeenId, size)
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getName());
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
    public List<ProductDto> getProductWithVendors(String productName) {
        List<Product> products = productRepository.findProductWithVendors(productName);

        return products.stream()
                .peek(this::validateVendorUrl)  // Product의 vendorUrl 검증
                .map(product -> {
                    // Price 테이블을 통해 Vendor를 조회
                    Optional<Vendor> vendor = vendorRepository.findByPricesProductId(product.getId());
                    vendor.ifPresent(this::validateUrl);
                    return ProductConverter.toProductDTO(product);
                })
                .collect(Collectors.toList());
    }

    // 랜덤 추천
    public  List<ProductDto> findRandomProducts() {
        List<ProductDto> products = productRepository.findRandomProducts()
                .stream()
                .map(product -> {
                    ProductDto productDto = ProductConverter.toProductDTO(product);
                    String imageUrl = s3Service.getProductImageUrl(product.getName());
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
