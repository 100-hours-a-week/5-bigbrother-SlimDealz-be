package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.entity.Bookmark;
import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, QuerydslPredicateExecutor<Bookmark>, BookmarkRepositoryCustom {
    @Query("SELECT b FROM Bookmark b JOIN FETCH b.product p JOIN FETCH p.prices WHERE b.member.id = :memberId")
    List<Bookmark> findBookmarksWithProductsAndPrices(Long memberId);
    @Query("SELECT b FROM Bookmark b JOIN b.product p WHERE b.member.id = :memberId AND p.productName = :productName")
    List<Bookmark> findBookmarksByMemberIdAndProductName(Long memberId, String productName);

    Optional<Bookmark> findByMemberAndProduct(Member member, Product product);
}
