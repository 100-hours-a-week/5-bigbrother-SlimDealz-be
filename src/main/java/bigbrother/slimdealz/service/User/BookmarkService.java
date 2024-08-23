package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.dto.BookmarkDto;
import bigbrother.slimdealz.entity.Bookmark;
import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import bigbrother.slimdealz.repository.User.BookmarkRepository;
import bigbrother.slimdealz.repository.User.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public List<BookmarkDto> getUserBookmarks(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findByMemberId(userId);
        return bookmarks.stream()
                .map(this::convertToBookmarkDto)
                .collect(Collectors.toList());
    }

    public BookmarkDto addBookmark(Long userId, BookmarkDto bookmarkDto) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(bookmarkDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .product(product)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return convertToBookmarkDto(savedBookmark);
    }

    public void deleteBookmark(Long userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findByIdAndMemberId(bookmarkId, userId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));
        bookmarkRepository.delete(bookmark);
    }

    private BookmarkDto convertToBookmarkDto(Bookmark bookmark) {
        return BookmarkDto.builder()
                .id(bookmark.getId())
                .userId(bookmark.getMember().getId())
                .productId(bookmark.getProduct().getId())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}