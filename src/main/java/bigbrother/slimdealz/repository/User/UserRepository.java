package bigbrother.slimdealz.repository.User;

import bigbrother.slimdealz.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE m.kakao_Id = :kakaoId")
    Optional<Member> findByKakao_Id(String kakaoId);
}
