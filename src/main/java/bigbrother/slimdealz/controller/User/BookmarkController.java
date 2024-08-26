package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.dto.user.BookmarkDto;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.service.User.BookmarkService;
import bigbrother.slimdealz.service.User.UserService;  // Assuming you have a UserService to handle user-related logic
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserService userService;

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

    @GetMapping("/recommendations")
    public ResponseEntity<List<Product>> recommendProductsForUser(@PathVariable Long userId) {
        List<Product> recommendedProducts = bookmarkService.recommendProductsForUser(userId);
        return ResponseEntity.ok(recommendedProducts);
    }
}