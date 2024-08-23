package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.MemberRole;
import bigbrother.slimdealz.dto.MemberDTO;
import bigbrother.slimdealz.dto.MemberUpdateDTO;
import bigbrother.slimdealz.repository.User.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public Member saveMember(MemberDTO memberDTO) {
        Member member = Member.builder()
                .name(memberDTO.getName())
                .kakao_Id(memberDTO.getKakao_Id())
                .profileImage(memberDTO.getProfileImage())  // 필드 이름을 일관되게 변경
                .nickname(memberDTO.getNickname())
                .card(memberDTO.getCard())
                .notification_agree(memberDTO.isNotification_agree())
                .role(MemberRole.USER)
                .build();
        return memberRepository.save(member);
    }
    //MemberService에 업데이트 메서드 추가:
    public Member updateMemberProfile(String kakao_Id, MemberDTO memberDTO) {
        Optional<Member> optionalMember = memberRepository.findByKakaoId(kakao_Id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setNickname(memberDTO.getNickname());
            member.setCard(memberDTO.getCard());
            member.setNotification_agree(memberDTO.isNotification_agree());

            return memberRepository.save(member);
        } else {
            throw new RuntimeException("User not found with kakao_Id: " + kakao_Id);
        }
    }

}
