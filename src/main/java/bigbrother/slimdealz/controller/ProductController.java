package bigbrother.slimdealz.controller;

import bigbrother.slimdealz.dto.ProductDto;
import bigbrother.slimdealz.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/search")
    public List<ProductDto> searchProducts(@RequestParam("keyword") String keyword) {
        return productService.searchProducts(keyword);
    }
}

