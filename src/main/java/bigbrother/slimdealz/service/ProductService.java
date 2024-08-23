<<<<<<< Updated upstream
package bigbrother.slimdealz.service;

import bigbrother.slimdealz.dto.ProductConverter;
import bigbrother.slimdealz.dto.ProductDto;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
>>>>>>> Stashed changes
=======
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
>>>>>>> Stashed changes
import bigbrother.slimdealz.repository.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
    public List<ProductDto> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword)
                .stream()
                .map(ProductConverter::toProductDTO) //converter 를 통해 DTO 로 변환
                .collect(Collectors.toList()); // stream의 변환된 요소들을 리스트로 반환
    }

    public List<ProductDto> findLowestPriceProducts() {
        return productRepository.findLowestPriceProducts()
                .stream()
                .map(ProductConverter::toProductDTO)
                .collect(Collectors.toList());
=======
=======
>>>>>>> Stashed changes
    // 상품 검색
    public List<ProductDto> searchProducts(String keyword) {
        List<ProductDto> products = productRepository.searchByKeyword(keyword)
                .stream()
                .map(ProductConverter::toProductDTO) //converter 를 통해 DTO 로 변환
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
                .map(ProductConverter::toProductDTO)
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
        return ProductConverter.toProductDTO(product);
    }

    // 카테고리 별 상품 조회
    public List<ProductDto> findByCategory(String category) {
        List<ProductDto> products = productRepository.findByCategory(category)
                .stream()
                .map(ProductConverter::toProductDTO)
                .collect(Collectors.toList());

        if(products.isEmpty()) {
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
        return products;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    }
}
=======
//package bigbrother.slimdealz.service;
//
//import bigbrother.slimdealz.dto.ProductConverter;
//import bigbrother.slimdealz.dto.ProductDto;
//import bigbrother.slimdealz.repository.Product.ProductRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ProductService {
//    private final ProductRepository productRepository;
//
//    public List<ProductDto> searchProducts(String keyword) {
//        return productRepository.searchByKeyword(keyword)
//                .stream()
//                .map(ProductConverter::toProductDTO) //converter 를 통해 DTO 로 변환
//                .collect(Collectors.toList()); // stream의 변환된 요소들을 리스트로 반환
//    }
//
//    public List<ProductDto> findLowestPriceProducts() {
//        return productRepository.findLowestPriceProducts()
//                .stream()
//                .map(ProductConverter::toProductDTO)
//                .collect(Collectors.toList());
//    }
//}
>>>>>>> Stashed changes
