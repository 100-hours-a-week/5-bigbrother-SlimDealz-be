package bigbrother.slimdealz.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomErrorResponse {
    private CustomErrorCode code;
    private String message;
}
