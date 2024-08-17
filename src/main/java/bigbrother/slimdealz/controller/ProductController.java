package bigbrother.slimdealz.controller;

import bigbrother.slimdealz.entity.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductController productService;

    public ProductController(ProductController productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public List<Product> searchByKeyword(String keyword) {
        return productService.searchByKeyword(keyword);
    }

    @GetMapping("/search/meaning")
    public List<Product> searchByMeaning(String keyword) {
        return productService.searchByMeaning(keyword);
    }

    @GetMapping("/search/converted")
    public List<Product> searchByKoreanKeyword(String keyword) {
        return productService.searchByKoreanKeyword(keyword);
    }
}
