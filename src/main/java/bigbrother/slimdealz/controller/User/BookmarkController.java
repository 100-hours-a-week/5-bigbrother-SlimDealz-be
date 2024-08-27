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

//    // New endpoint to get userId based on Kakao_ID
//    @GetMapping("/kakao/{kakao_Id}/id")
//    public ResponseEntity<Long> getUserIdByKakaoId(@PathVariable String kakao_Id) {
//        Long userId = userService.findUserIdByKakao_Id(kakao_Id);
//        if (userId != null) {
//            return ResponseEntity.ok(userId);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
//
//    @GetMapping("/{userId}/bookmarks")
//    public ResponseEntity<List<BookmarkProductPriceDto>> getUserBookmarks(@PathVariable Long userId) {
//        List<BookmarkProductPriceDto> bookmarks = bookmarkService.getUserBookmarksWithPrice(userId);
//        return ResponseEntity.ok(bookmarks);
//    }
//
//    @PostMapping("/{userId}/bookmarks/{productName}")
//    public ResponseEntity<BookmarkDto> addBookmarkByProductName(@PathVariable Long userId, @PathVariable String productName) {
//        BookmarkDto createdBookmark = bookmarkService.addBookmarkByProductName(userId, productName);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookmark);
//    }

    @GetMapping("/kakao/{kakao_Id}/bookmarks")
    public ResponseEntity<List<BookmarkProductPriceDto>> getUserBookmarksByKakaoId(@PathVariable String kakao_Id) {
        Long userId = userService.findUserIdByKakao_Id(kakao_Id);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<BookmarkProductPriceDto> bookmarks = bookmarkService.getUserBookmarksWithPrice(userId);
        return ResponseEntity.ok(bookmarks);
    }

    @PostMapping("/kakao/{kakao_Id}/bookmarks/{productName}")
    public ResponseEntity<BookmarkDto> addBookmarkByKakaoId(@PathVariable String kakao_Id, @PathVariable String productName) {
        Long userId = userService.findUserIdByKakao_Id(kakao_Id);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BookmarkDto createdBookmark = bookmarkService.addBookmarkByProductName(userId, productName);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBookmark);
    }

}
