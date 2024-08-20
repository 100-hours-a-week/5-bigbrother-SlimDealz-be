package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.auth.JWTConstants;
import bigbrother.slimdealz.auth.JWTutil;
import bigbrother.slimdealz.dto.MemberDTO;
import bigbrother.slimdealz.entity.KakaoUserInfo;
import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.service.User.MemberService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@RestController
public class KakaoAuthController {

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;

    @Value("${KAKAO_REDIRECT_URL}")
    private String kakaoRedirectUrl;

    @Value("${CLIENT_URL}")
    private String client_Url;

    private final MemberService memberService;

    public KakaoAuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> handleKakaoCallback(@RequestParam("code") String code) {
        // 1. 카카오 서버에 액세스 토큰 및 리프레시 토큰 요청
        Map<String, Object> tokens = getKakaoAccessToken(code);

        String accessToken = (String) tokens.get("access_token");
        String refreshToken = (String) tokens.get("refresh_token");

        // 2. 액세스 토큰을 사용하여 사용자 프로필 정보 가져오기
        ResponseEntity<String> userProfileResponse = getUserProfile(accessToken);
        Map<String, Object> userProfile = new Gson().fromJson(userProfileResponse.getBody(), Map.class);

        // 3. KakaoUserInfo 객체로 사용자 정보 추출
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(userProfile);

        // 4. 기존 회원 확인 및 등록 또는 업데이트
        Optional<Member> existingMember = memberService.findBySocialId(kakaoUserInfo.getSocialId());
        Member member;
        if (existingMember.isPresent()) {
            // 기존 회원이 존재하는 경우, 업데이트 로직을 추가할 수 있습니다.
            member = existingMember.get();
            // 필요한 경우, 회원 정보를 업데이트
            // memberService.updateMember(member, kakaoUserInfo);
        } else {
            // 기존 회원이 없는 경우, 새로 등록
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setName(kakaoUserInfo.getName());
            memberDTO.setSocialId(kakaoUserInfo.getSocialId());
            memberDTO.setProfileImage(kakaoUserInfo.getProfileImage());

            member = memberService.saveMember(memberDTO);
        }

        // 5. JWT 토큰 생성 (JwtUtils 사용)
        Map<String, Object> claims = Map.of(
                "socialId", member.getSocialId(),
                "name", member.getName(),
                "role", member.getRole().getValue(), // 사용자 역할 설정
                "profile_image", member.getProfileImage()
        );
        String jwtToken = JWTutil.generateToken(claims, JWTConstants.ACCESS_EXP_TIME);
        System.out.print(member.getRole());
        System.out.print(member.getProfileImage());
        // 6. 리디렉션 URL을 정확히 문자열로 생성
        String redirectUrl = client_Url + "/signup?" +
                "jwtToken=" + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8) +
                "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    private Map<String, Object> getKakaoAccessToken(String authCode) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);  // 카카오에서 발급받은 REST API 키
        params.add("redirect_uri", kakaoRedirectUrl);
        params.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // 응답에서 access_token 및 refresh_token을 포함한 JSON 데이터를 추출
            Map<String, Object> json = new Gson().fromJson(response.getBody(), Map.class);
            return json;
        }

        throw new RuntimeException("Failed to get tokens from Kakao");
    }

    private ResponseEntity<String> getUserProfile(String accessToken) {
        String profileUri = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(profileUri, HttpMethod.GET, entity, String.class);
    }
}
