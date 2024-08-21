package bigbrother.slimdealz.entity;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "bookmark")
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
    @JoinColumn(name = "user_id")
    private Member member;  // Member 엔티티와 연관

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, updatable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    private Timestamp deletedAt;

    // Getters and Setters
}
