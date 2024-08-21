package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMemberId(Long memberId);
}
