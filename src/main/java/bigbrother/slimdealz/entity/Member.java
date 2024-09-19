package bigbrother.slimdealz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String kakao_Id;

    @Column(name = "profile_image") // Map to the database column
    private String profileImage;

    private String kakaoAccessToken;
    private String kakaoRefreshToken;

    private LocalDateTime kakaoAccessTokenExpiresAt;
    private LocalDateTime kakaoRefreshTokenExpiresAt;

    private String nickname;
    private String card;
    private boolean notification_agree; // 올바른 철자로 변경

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String fcmToken;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}