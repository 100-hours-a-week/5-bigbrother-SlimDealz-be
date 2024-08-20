package bigbrother.slimdealz.service.User;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.MemberRole;
import bigbrother.slimdealz.dto.MemberDTO;
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

    public Optional<Member> findBySocialId(String socialId) {
        return memberRepository.findBySocialId(socialId);
    }

    public Member saveMember(MemberDTO memberDTO) {
        Member member = Member.builder()
                .name(memberDTO.getName())
                .socialId(memberDTO.getSocialId())
                .profileImage(memberDTO.getProfileImage())  // 필드 이름을 일관되게 변경
                .nickname(memberDTO.getNickname())
                .cardInfo(memberDTO.getCardInfo())
                .role(MemberRole.USER)
                .build();
        return memberRepository.save(member);
    }
}
