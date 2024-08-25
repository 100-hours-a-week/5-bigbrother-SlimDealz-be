package bigbrother.slimdealz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
    private Long id;
    private Long userId;
    private Long productId;
    private Timestamp createdAt;
}