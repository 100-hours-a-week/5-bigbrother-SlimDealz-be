package bigbrother.slimdealz.dto.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
    private Long id;            // 회원 번호
    private String name;      // 카카오에서 가져온 닉네임
    private String kakao_Id;      // 카카오 사용자 ID
    private String profileImage;  // 카카오 프로필 이미지 URL
    private String card;
    private String nickname;
    private boolean notification_agree; // 알림 설정
}
