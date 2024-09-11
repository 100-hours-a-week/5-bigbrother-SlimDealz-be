package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.MemberRole;
import bigbrother.slimdealz.dto.user.MemberDTO;
import bigbrother.slimdealz.repository.User.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByKakaoId(String kakao_Id) {
        return memberRepository.findByKakaoId(kakao_Id);
    }

    public Member findMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public Member saveMember(MemberDTO memberDTO) {
        Member member = Member.builder()
                .name(memberDTO.getName())
                .kakao_Id(memberDTO.getKakao_Id())
                .profileImage(memberDTO.getProfileImage())
                .nickname(memberDTO.getNickname())
                .card(memberDTO.getCard())
                .notification_agree(memberDTO.isNotification_agree())
                .role(MemberRole.USER)
                .kakaoAccessToken(memberDTO.getKakaoAccessToken())  // 추가된 필드 설정
                .kakaoAccessTokenExpiresAt(memberDTO.getKakaoAccessTokenExpiresAt())
                .kakaoRefreshToken(memberDTO.getKakaoRefreshToken())
                .kakaoRefreshTokenExpiresAt(memberDTO.getKakaoRefreshTokenExpiresAt())
                .build();
        return memberRepository.save(member);
    }

    public Member updateMemberProfile(String kakao_Id, MemberDTO memberDTO) {
        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakao_Id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setNickname(memberDTO.getNickname());
            member.setCard(memberDTO.getCard());
            member.setNotification_agree(memberDTO.isNotification_agree());

            // 카카오 토큰 업데이트 로직 추가
            member.setKakaoAccessToken(memberDTO.getKakaoAccessToken());
            member.setKakaoAccessTokenExpiresAt(memberDTO.getKakaoAccessTokenExpiresAt());
            member.setKakaoRefreshToken(memberDTO.getKakaoRefreshToken());
            member.setKakaoRefreshTokenExpiresAt(memberDTO.getKakaoRefreshTokenExpiresAt());

            return memberRepository.save(member);
        } else {
            throw new RuntimeException("User not found with kakao_Id: " + kakao_Id);
        }
    }

    /**
     * FCM 토큰 저장 또는 업데이트 메서드
     *
     * @param userId   사용자 ID
     * @param fcmToken FCM 토큰
     * @return 업데이트된 사용자
     */
    public Member saveOrUpdateFcmToken(Long userId, String fcmToken) {
        // 사용자 조회
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // FCM 토큰 업데이트
        member.setFcmToken(fcmToken);
        return memberRepository.save(member);  // 변경된 정보 저장
    }

}