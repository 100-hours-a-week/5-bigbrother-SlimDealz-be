package bigbrother.slimdealz.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private String productName;
    private int customerRating;
    private String content;
    private String productSource;
    private LocalDate reviewDate;
    private String userName;
}
