package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.dto.product.ProductDto;
import bigbrother.slimdealz.dto.user.BookmarkDto;
import bigbrother.slimdealz.dto.user.BookmarkProductPriceDto;
import bigbrother.slimdealz.service.ProductService;
import bigbrother.slimdealz.service.User.BookmarkService;
import bigbrother.slimdealz.service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserService userService;
    private final ProductService productService;

    // 북마크 목록 조회 (JWT로 인증된 사용자)
    @GetMapping("/bookmarks")
    public ResponseEntity<List<BookmarkProductPriceDto>> getUserBookmarks(HttpServletRequest request) {
        // JWT에서 추출된 kakao_Id를 요청에서 가져옴
        String kakaoId = (String) request.getAttribute("kakao_Id");

        Long id = userService.findUserIdByKakaoId(kakaoId);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<BookmarkProductPriceDto> bookmarks = bookmarkService.getUserBookmarksWithPrice(id);
        return ResponseEntity.ok(bookmarks);
    }

    // 특정 상품이 북마크되어 있는지 확인 (JWT로 인증된 사용자)
    @GetMapping("/bookmarks/search")
    public ResponseEntity<Boolean> isProductBookmarked(
            HttpServletRequest request,
            @RequestParam("productName") String productName) {
        // JWT에서 추출된 kakao_Id를 요청에서 가져옴
        String kakaoId = (String) request.getAttribute("kakao_Id");

        Long id = userService.findUserIdByKakaoId(kakaoId);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean isBookmarked = bookmarkService.isProductBookmarked(id, productName);
        return ResponseEntity.ok(isBookmarked);
    }

    // 북마크 추가 (JWT로 인증된 사용자)
    @PostMapping("/bookmarks")
    public ResponseEntity<BookmarkDto> addBookmarkByKakaoId(
            HttpServletRequest request,
            @RequestBody BookmarkProductPriceDto bookmarkProductPriceDto) {
        // JWT에서 추출된 kakao_Id를 요청에서 가져옴
        String kakaoId = (String) request.getAttribute("kakaoId");

        Long id = userService.findUserIdByKakaoId(kakaoId);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BookmarkDto createdBookmark = bookmarkService.addBookmarkByProductName(id, bookmarkProductPriceDto.getProductName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookmark);
    }

    // 북마크 삭제 (JWT로 인증된 사용자)
    @DeleteMapping("/bookmarks")
    public ResponseEntity<Void> deleteBookmarkByKakaoId(
            HttpServletRequest request,
            @RequestParam("productName") String productName) {
        // JWT에서 추출된 kakao_Id를 요청에서 가져옴
        String kakaoId = (String) request.getAttribute("kakao_Id");

        Long id = userService.findUserIdByKakaoId(kakaoId);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        bookmarkService.removeBookmarkByProductName(id, productName);
        return ResponseEntity.noContent().build();
    }

    // 북마크 기반 상품 추천
    @GetMapping("/bookmarks/recommend-products")
    public ResponseEntity<List<ProductDto>> getRecommendedProductsByBookmark(HttpServletRequest request) {
        try {
            String kakaoId = (String) request.getAttribute("kakao_Id");

            Long id = userService.findUserIdByKakaoId(kakaoId);
            if (id == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            List<ProductDto> recommendedProducts = productService.getPopularProducts();

            return ResponseEntity.ok(recommendedProducts);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
