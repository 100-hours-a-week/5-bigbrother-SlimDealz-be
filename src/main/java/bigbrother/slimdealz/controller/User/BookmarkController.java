package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.dto.PriceDto;
import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.dto.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.repository.User.MemberRepository;
import bigbrother.slimdealz.service.User.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final MemberRepository memberRepository;

    @GetMapping("/{socialId}/bookmarks")
    public ResponseEntity<List<ProductDto>> getUserBookmarks(@PathVariable String socialId) {
        Member member = memberRepository.findBySocialId(socialId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> bookmarkedProducts = bookmarkService.getUserBookmarks(member.getId());

        List<ProductDto> productDTOs = bookmarkedProducts.stream()
                .map(this::convertToProductDto) // 변환 메서드 사용
                .collect(Collectors.toList());

        return ResponseEntity.ok(productDTOs);
    }

    // Product -> ProductDto 변환 메서드
    private ProductDto convertToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .image(product.getImage())
                .brand(product.getBrand())
                .category(product.getCategory())
                .shippingFee(product.getShippingFee() != null ? product.getShippingFee().toString() : "무료 배송")
                .prices(product.getPrices().stream()
                        .map(price -> PriceDto.builder()
                                .id(price.getId())
                                .setPrice(price.getSetPrice())
                                .discountedPrice(price.getDiscountedPrice())
                                .promotion(price.getPromotion())
                                .productId(price.getProduct().getId())
                                .vendorId(price.getVendor().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}