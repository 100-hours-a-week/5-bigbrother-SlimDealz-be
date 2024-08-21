<<<<<<< Updated upstream
package bigbrother.slimdealz.service;

import bigbrother.slimdealz.dto.ProductConverter;
import bigbrother.slimdealz.dto.ProductDto;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

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
