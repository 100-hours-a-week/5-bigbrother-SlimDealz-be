//package bigbrother.slimdealz;
//
//import bigbrother.slimdealz.entity.product.Product;
//import bigbrother.slimdealz.repository.Product.ProductRepositoryImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//class SlimDealzApplicationTests {
//
//    @Autowired
//    private ProductRepositoryImpl productRepository;
//
//    @Test
//    void contextLoads() {
//    }
//
//    @Test
//    public void testFindProductWithVendors() {
//        List<Product> products = productRepository.findProductWithVendors("하림 닭가슴살 블랙페퍼 100g (1개)");
//
//        assertFalse(products.isEmpty());
//        assertNotNull(products.get(0).getPrices().get(0).getVendor());
//        System.out.println(products.get(0).getPrices().get(0).getVendor().getVendorName());
//    }
//
//
//}
