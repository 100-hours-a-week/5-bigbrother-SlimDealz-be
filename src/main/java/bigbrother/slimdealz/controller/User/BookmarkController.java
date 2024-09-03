package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.dto.user.BookmarkDto;
import bigbrother.slimdealz.dto.user.BookmarkProductPriceDto;
import bigbrother.slimdealz.service.User.BookmarkService;
import bigbrother.slimdealz.service.User.UserService;
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

    @GetMapping("/kakao/{kakao_Id}/bookmarks")
    public ResponseEntity<List<BookmarkProductPriceDto>> getUserBookmarksByKakaoId(@PathVariable String kakao_Id) {
        Long userId = userService.findUserIdByKakao_Id(kakao_Id);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<BookmarkProductPriceDto> bookmarks = bookmarkService.getUserBookmarksWithPrice(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @GetMapping("/kakao/{kakao_Id}/bookmarks/search")
    public ResponseEntity<Boolean> isProductBookmarked(
            @PathVariable String kakao_Id,
            @RequestParam(required = true) String productName) {
        Long userId = userService.findUserIdByKakao_Id(kakao_Id);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean isBookmarked = bookmarkService.isProductBookmarked(userId, productName);
        if (isBookmarked) {
            return ResponseEntity.ok(true);
        } else {
            // 북마크가 없을 경우 204 No Content 반환
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PostMapping("/kakao/{kakao_Id}/bookmarks")
    public ResponseEntity<BookmarkDto> addBookmarkByKakaoId(
            @PathVariable String kakao_Id,
            @RequestBody BookmarkProductPriceDto bookmarkProductPriceDto) {
        Long userId = userService.findUserIdByKakao_Id(kakao_Id);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BookmarkDto createdBookmark = bookmarkService.addBookmarkByProductName(userId, bookmarkProductPriceDto.getProductName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookmark);
    }

    @DeleteMapping("/kakao/{kakao_Id}/bookmarks")
    public ResponseEntity<Void> deleteBookmarkByKakaoId(
            @PathVariable String kakao_Id,
            @RequestParam("productName") String productName) {
        Long userId = userService.findUserIdByKakao_Id(kakao_Id);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        bookmarkService.removeBookmarkByProductName(userId, productName);
        return ResponseEntity.noContent().build();
    }
}
