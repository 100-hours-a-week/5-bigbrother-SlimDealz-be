package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< Updated upstream
<<<<<<< Updated upstream

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMemberId(Long memberId);
=======
=======
>>>>>>> Stashed changes
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByMemberId(Long memberId);
    Optional<Bookmark> findByIdAndMemberId(Long id, Long memberId);
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
}
