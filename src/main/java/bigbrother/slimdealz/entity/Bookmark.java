package bigbrother.slimdealz.entity;

<<<<<<< Updated upstream
<<<<<<< Updated upstream
import bigbrother.slimdealz.entity.Member;
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
import bigbrother.slimdealz.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
<<<<<<< Updated upstream
<<<<<<< Updated upstream
@Table(name = "bookmark")
=======
@Table(name = "bookmarks")
>>>>>>> Stashed changes
=======
@Table(name = "bookmarks")
>>>>>>> Stashed changes
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    @JoinColumn(name = "user_id")
    private Member member;  // Member 엔티티와 연관

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    private Timestamp deletedAt;

    // Getters and Setters
=======
=======
>>>>>>> Stashed changes
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;  // Reference to the Member entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;  // Reference to the Product entity

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
}
