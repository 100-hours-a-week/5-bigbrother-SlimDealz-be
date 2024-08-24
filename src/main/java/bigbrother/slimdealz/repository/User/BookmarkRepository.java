package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.dto.BookmarkProductPriceDto;
import bigbrother.slimdealz.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByMemberId(Long memberId);

    Optional<Bookmark> findByIdAndMemberId(Long id, Long memberId);

    @Query("SELECT b FROM Bookmark b JOIN FETCH b.product p JOIN FETCH p.prices WHERE b.member.id = :memberId")
    List<Bookmark> findBookmarksWithProductsAndPrices(Long memberId);
}