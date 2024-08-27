package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.dto.BookmarkDto;
import bigbrother.slimdealz.dto.BookmarkProductPriceDto;
import bigbrother.slimdealz.dto.PriceDto;
import bigbrother.slimdealz.entity.Bookmark;
import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.repository.Product.ProductRepository;
import bigbrother.slimdealz.repository.User.BookmarkRepository;
import bigbrother.slimdealz.repository.User.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
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


    public List<BookmarkProductPriceDto> getUserBookmarksWithPrice(Long userId) {
        // 북마크와 관련된 모든 엔티티들을 가져옵니다.
        List<Bookmark> bookmarks = bookmarkRepository.findBookmarksWithProductsAndPrices(userId);

        // 각 엔티티를 DTO로 변환합니다.
        return bookmarks.stream().map(bookmark -> {
            List<PriceDto> prices = bookmark.getProduct().getPrices().stream()
                    .map(price -> PriceDto.builder()
                            .id(price.getId())
                            .setPrice(price.getSetPrice())
                            .promotion(price.getPromotion())
                            .productId(price.getProduct().getId())
                            .vendorId(price.getVendor().getId())
                            .build())
                    .collect(Collectors.toList());

            return BookmarkProductPriceDto.builder()
                    .bookmarkId(bookmark.getId())
                    .productId(bookmark.getProduct().getId())
                    .productName(bookmark.getProduct().getName())
                    .shippingFee(bookmark.getProduct().getShippingFee())
                    .prices(prices)
                    .build();
        }).collect(Collectors.toList());
    }


    public BookmarkDto addBookmarkByProductName(Long userId, String productName) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // productName으로 모든 상품을 검색하고, 가장 최저가를 가진 상품을 선택합니다.
        List<Product> products = productRepository.findByName(productName);

        if (products.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        // 최저가를 가진 상품 찾기
        Product cheapestProduct = products.stream()
                .min(Comparator.comparing(product -> product.getPrices().stream()
                        .min(Comparator.comparing(price -> price.getSetPrice()))
                        .orElseThrow(() -> new RuntimeException("Price not found")).getSetPrice()))
                .orElseThrow(() -> new RuntimeException("Cheapest product not found"));

        // 북마크 생성
        Bookmark bookmark = Bookmark.builder()
                .member(member)
                .product(cheapestProduct)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        return convertToBookmarkDto(savedBookmark);
    }
}