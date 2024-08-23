package bigbrother.slimdealz.controller.User;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
import bigbrother.slimdealz.dto.PriceDto;
import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.dto.ProductDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.repository.User.MemberRepository;
import bigbrother.slimdealz.service.User.BookmarkService;
import lombok.RequiredArgsConstructor;
=======
=======
>>>>>>> Stashed changes
import bigbrother.slimdealz.dto.BookmarkDto;
import bigbrother.slimdealz.service.User.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
=======

@RestController
@RequestMapping("/api/v1/users/{userId}/bookmarks")
>>>>>>> Stashed changes
=======

@RestController
@RequestMapping("/api/v1/users/{userId}/bookmarks")
>>>>>>> Stashed changes
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
<<<<<<< Updated upstream
<<<<<<< Updated upstream
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
=======
=======
>>>>>>> Stashed changes

    @GetMapping
    public ResponseEntity<List<BookmarkDto>> getUserBookmarks(@PathVariable Long userId) {
        List<BookmarkDto> bookmarks = bookmarkService.getUserBookmarks(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @PostMapping
    public ResponseEntity<BookmarkDto> addBookmark(@PathVariable Long userId, @RequestBody BookmarkDto bookmarkDto) {
        BookmarkDto createdBookmark = bookmarkService.addBookmark(userId, bookmarkDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookmark);
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long userId, @PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(userId, bookmarkId);
        return ResponseEntity.noContent().build();
    }
<<<<<<< Updated upstream
}
>>>>>>> Stashed changes
=======
}
>>>>>>> Stashed changes
