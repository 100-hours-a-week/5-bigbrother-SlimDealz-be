package bigbrother.slimdealz.fcm.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FCMMessageRequestDto {

    private Long id;

    private String title;

    private String body;

    private String token;
}