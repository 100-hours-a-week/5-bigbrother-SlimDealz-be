package bigbrother.slimdealz.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
    private String name;      // 카카오에서 가져온 닉네임
    private String socialId;      // 카카오 사용자 ID
    private String profileImage;  // 카카오 프로필 이미지 URL
    private String cardInfo;
    private String nickname;
    private boolean recieveNotification; // 알림 설정

}
