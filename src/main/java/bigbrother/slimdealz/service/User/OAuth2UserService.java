package bigbrother.slimdealz.service.User;


import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.MemberRole;
import bigbrother.slimdealz.auth.PrincipalDetail;
import bigbrother.slimdealz.entity.KakaoUserInfo;
import bigbrother.slimdealz.repository.User.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("--------------------------- OAuth2UserService ---------------------------");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("OAuth2USer = {}", oAuth2User);
        log.info("attributes = {}", attributes);

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        log.info("nameAttributeKey = {}", userNameAttributeName);

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
        String socialId = kakaoUserInfo.getSocialId();
        String name = kakaoUserInfo.getName();

        // 소셜 ID 로 사용자를 조회, 없으면 socialId 와 이름으로 사용자 생성
        Optional<Member> bySocialId = memberRepository.findBySocialId(socialId);
        Member member = bySocialId.orElseGet(() -> saveSocialMember(socialId, name));

        return new PrincipalDetail(member, Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue())),
                attributes);
    }

    // 소셜 ID 로 가입된 사용자가 없으면 새로운 사용자를 만들어 저장한다
    public Member saveSocialMember(String socialId, String name) {
        log.info("--------------------------- saveSocialMember ---------------------------");
        Member newMember = Member.builder().socialId(socialId).name(name).role(MemberRole.USER).build();
        return memberRepository.save(newMember);
    }
}
