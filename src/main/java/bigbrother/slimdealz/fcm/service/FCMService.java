package bigbrother.slimdealz.fcm.service;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.repository.User.MemberRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    private final MemberRepository memberRepository;

    public FCMService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveOrUpdateToken(Long id, String token) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));

        member.setFcmToken(token); // FCM 토큰 업데이트
        memberRepository.save(member);
    }
}