<<<<<<< Updated upstream
package bigbrother.slimdealz.controller;

import bigbrother.slimdealz.dto.ProductDto;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import bigbrother.slimdealz.service.ProductService;
import lombok.RequiredArgsConstructor;
=======
=======
>>>>>>> Stashed changes
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.exception.CustomErrorCode;
import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
import org.springframework.web.bind.annotation.*;

import java.util.List;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
@RestController
@RequestMapping("/api/v1")
=======
@Slf4j
@RestController
@RequestMapping("/v1")
>>>>>>> Stashed changes
=======
@Slf4j
@RestController
@RequestMapping("/v1")
>>>>>>> Stashed changes
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/search")
    public List<ProductDto> searchProducts(@RequestParam("keyword") String keyword) {
<<<<<<< Updated upstream
<<<<<<< Updated upstream
        return productService.searchProducts(keyword);
=======
=======
>>>>>>> Stashed changes
        try {
            return productService.searchProducts(keyword);
        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.SEARCH_NO_RESULT);
        }
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    }

    @GetMapping("/today-lowest-products")
    public List<ProductDto> findLowestPriceProducts() {
<<<<<<< Updated upstream
<<<<<<< Updated upstream
        return productService.findLowestPriceProducts();
    }
}

=======
//package bigbrother.slimdealz.controller;
//
//import bigbrother.slimdealz.dto.ProductDto;
//import bigbrother.slimdealz.service.ProductService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1")
//@RequiredArgsConstructor
//public class ProductController {
//
//    private final ProductService productService;
//
//    @GetMapping("/search")
//    public List<ProductDto> searchProducts(@RequestParam("keyword") String keyword) {
//        return productService.searchProducts(keyword);
//    }
//
//    @GetMapping("/today-lowest-products")
//    public List<ProductDto> findLowestPriceProducts() {
//        return productService.findLowestPriceProducts();
//    }
//}
//
>>>>>>> Stashed changes
=======
        try{
            return productService.findLowestPriceProducts();
        }
        catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @GetMapping("/product-detail")
    public ProductDto getProductWithLowestPriceByName(@RequestParam("productName") String productName) {
        try {
            return productService.getProductWithLowestPriceByName(productName);
        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @GetMapping("/products")
    public List<ProductDto> findByCategory(@RequestParam("category") String category) {
        try {
            return productService.findByCategory(category);
        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }
}

>>>>>>> Stashed changes
=======
        try{
            return productService.findLowestPriceProducts();
        }
        catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @GetMapping("/product-detail")
    public ProductDto getProductWithLowestPriceByName(@RequestParam("productName") String productName) {
        try {
            return productService.getProductWithLowestPriceByName(productName);
        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    @GetMapping("/products")
    public List<ProductDto> findByCategory(@RequestParam("category") String category) {
        try {
            return productService.findByCategory(category);
        } catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(CustomErrorCode.PRODUCT_NOT_FOUND);
        }
    }
}

>>>>>>> Stashed changes
