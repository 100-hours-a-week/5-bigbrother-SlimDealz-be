package bigbrother.slimdealz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name= "users")
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
@Setter
>>>>>>> Stashed changes
=======

>>>>>>> Stashed changes
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String kakao_Id;
    private String profileImage;
    private String nickname;
    private String card;
<<<<<<< Updated upstream

<<<<<<< Updated upstream
    private boolean receiveNotification; // 올바른 철자로 변경
=======
    private boolean notification_agree; // 올바른 철자로 변경
>>>>>>> Stashed changes
=======
    private boolean notification_agree; // 올바른 철자로 변경
>>>>>>> Stashed changes

    @Enumerated(EnumType.STRING)
    private MemberRole role;

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
