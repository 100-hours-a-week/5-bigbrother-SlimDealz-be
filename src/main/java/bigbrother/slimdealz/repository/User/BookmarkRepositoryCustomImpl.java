package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.entity.QBookmark;
import bigbrother.slimdealz.entity.QMember;
import bigbrother.slimdealz.entity.product.Product;
import bigbrother.slimdealz.entity.product.QProduct;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom{
    private  final EntityManager entityManager;

    @Override
    public List<Product> findRecommendedProducts(Long userId) {
        QBookmark bookmark = QBookmark.bookmark;
        QMember member = QMember.member;
        QProduct product = QProduct.product;

        // 사용자가 북마크한 상품
        JPAQuery<Long> userBookmarkQuery = new JPAQuery<>(entityManager);
        List<Long> userBookmarkProductIds = userBookmarkQuery
                .select(bookmark.product.id)
                .from(bookmark)
                .where(bookmark.member.id.eq(userId))
                .fetch();

        // 동일한 상품을 북마크한 다른 사용자들의 북마크 데이터 호출
        JPAQuery<com.querydsl.core.Tuple> otherUsersBookmarksQuery = new JPAQuery<>(entityManager);
        List<com.querydsl.core.Tuple> otherUsersBookmarks = otherUsersBookmarksQuery
                .select(bookmark.member.id, bookmark.product.id)
                .from(bookmark)
                .where(bookmark.product.id.in(userBookmarkProductIds)
                        .and(bookmark.member.id.ne(userId)))
                .fetch();

        // 다른 사용자가 북마크한 상품 중, 현재 사용자가 북마크 하지 않은 상품 필터링
        Set<Long> recommendedProductList = otherUsersBookmarks.stream()
                .map(tuple -> tuple.get(bookmark.product.id))
                .filter(productId -> !userBookmarkProductIds.contains(productId))
                .collect(Collectors.toSet());

        // 필터링된 상품 목록 반환
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        JPAQuery<Product> productQuery = new JPAQuery<>(entityManager);
        return productQuery
                .select(product)
                .from(product)
                .where(product.id.in(recommendedProductList)
                        .and(product.updatedAt.after(todayStart)))
                .fetch();
    }
}
