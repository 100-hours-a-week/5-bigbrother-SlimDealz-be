package bigbrother.slimdealz.service;

import bigbrother.slimdealz.dto.ProductConverter;
import bigbrother.slimdealz.dto.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import bigbrother.slimdealz.repository.Product.ProductRepositoryImpl;
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
                .map(ProductConverter::toProductDTO)
                .collect(Collectors.toList());
    }

//    public List<Product> searchByMeaning(String keyword) {
//        return productRepository.searchByMeaning(keyword);
//    }
//
//    public List<Product> searchByKoreanKeyword(String keyword) {
//        return productRepository.searchByKoreanKeyword(keyword);
//    }
}
