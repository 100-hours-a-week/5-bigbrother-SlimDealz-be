package bigbrother.slimdealz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String socialId;

    private String profileImage;  // 필드 이름을 일관되게 변경

    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
