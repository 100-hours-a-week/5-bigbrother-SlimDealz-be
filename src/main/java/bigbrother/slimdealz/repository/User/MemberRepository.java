package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.kakao_Id = :kakao_Id")
    Optional<Member> findByKakaoId(@Param("kakao_Id") String kakao_Id);
}