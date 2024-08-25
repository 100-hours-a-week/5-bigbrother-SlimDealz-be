package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.dto.BookmarkDto;
import bigbrother.slimdealz.dto.BookmarkProductPriceDto;
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

    // New endpoint to get userId based on Kakao_ID
    @GetMapping("/kakao/{kakao_Id}/id")
    public ResponseEntity<Long> getUserIdByKakaoId(@PathVariable String kakao_Id) {
        Long userId = userService.findUserIdByKakao_Id(kakao_Id);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{userId}/bookmarks")
    public ResponseEntity<List<BookmarkProductPriceDto>> getUserBookmarks(@PathVariable Long userId) {
        List<BookmarkProductPriceDto> bookmarks = bookmarkService.getUserBookmarksWithPrice(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @PostMapping("/{userId}/bookmarks")
    public ResponseEntity<BookmarkDto> addBookmark(@PathVariable Long userId, @RequestBody BookmarkDto bookmarkDto) {
        BookmarkDto createdBookmark = bookmarkService.addBookmark(userId, bookmarkDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookmark);
    }

    @DeleteMapping("/{userId}/bookmarks/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable Long userId, @PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(userId, bookmarkId);
        return ResponseEntity.noContent().build();
    }
}
