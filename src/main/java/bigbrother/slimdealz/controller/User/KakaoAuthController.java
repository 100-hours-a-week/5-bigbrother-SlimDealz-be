package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.auth.JWTConstants;
import bigbrother.slimdealz.auth.JWTutil;
import bigbrother.slimdealz.dto.user.MemberDTO;
import bigbrother.slimdealz.auth.KakaoUserInfo;
import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.service.User.MemberService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
public class KakaoAuthController {

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey; // 카카오 API 키

    @Value("${SERVER_URL}/auth/kakao/callback")
    private String kakaoRedirectUrl; // 카카오 인증 후 리다이렉트 URL

    @Value("${CLIENT_URL}")
    private String clientUrl; // 클라이언트 URL

    private final MemberService memberService;
    private final RestTemplate restTemplate;

    @Autowired
    public KakaoAuthController(MemberService memberService, RestTemplate restTemplate) {
        this.memberService = memberService;
        this.restTemplate = restTemplate;
    }

    /**
     * 카카오 인증 콜백 처리f
     *
     * @param code 카카오 인증 코드
     * @return 리다이렉션 응답
     */
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> handleKakaoCallback(@RequestParam("code") String code) {
        // 1. 카카오 서버에 액세스 토큰 요청
        Map<String, Object> tokens = getKakaoAccessToken(code);
        String accessToken = (String) tokens.get("access_token");
        String refreshToken = (String) tokens.get("refresh_token");
        int expiresIn = getExpiresIn(tokens); // 액세스 토큰의 만료 시간

        // 2. 액세스 토큰을 사용하여 사용자 프로필 정보 가져오기
        ResponseEntity<String> userProfileResponse = getUserProfile(accessToken);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(new Gson().fromJson(userProfileResponse.getBody(), Map.class));

        // 3. 기존 회원 확인 및 등록 또는 업데이트
        Member member = saveOrUpdateMember(kakaoUserInfo, accessToken, refreshToken, expiresIn);

        // 4. JWT 토큰 생성
        Map<String, Object> claims = Map.of(
                "id", member.getId(),
                "kakao_Id", member.getKakao_Id(),
                "name", member.getName(),
                "role", member.getRole().getValue(),
                "profile_image", member.getProfileImage()
        );
        String jwtToken = JWTutil.generateToken(claims, JWTConstants.ACCESS_EXP_TIME);
        String refreshTokenJwt = JWTutil.generateToken(claims, JWTConstants.REFRESH_EXP_TIME);

        // 5. 쿠키에 JWT 토큰, 리프레시 토큰, 카카오 ID 설정
        HttpHeaders headers = createCookies(jwtToken, refreshTokenJwt, member.getKakao_Id());

        // 6. 클라이언트로 리디렉션
        headers.setLocation(URI.create(clientUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    /**
     * 카카오 API를 통해 액세스 토큰 및 리프레시 토큰을 가져옴
     *
     * @param authCode 카카오 인증 코드
     * @return 액세스 토큰 및 리프레시 토큰을 포함한 맵
     */
    private Map<String, Object> getKakaoAccessToken(String authCode) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoApiKey);
        params.add("redirect_uri", kakaoRedirectUrl);
        params.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);


        if (response.getStatusCode() == HttpStatus.OK) {
            return new Gson().fromJson(response.getBody(), Map.class);
        }

        throw new RuntimeException("Failed to get tokens from Kakao");
    }

    /**
     * 토큰의 만료 시간을 가져옴
     *
     * @param tokens 토큰 정보를 담고 있는 맵
     * @return 만료 시간(초)
     */
    private int getExpiresIn(Map<String, Object> tokens) {
        Object expiresInObj = tokens.get("expires_in");
        if (expiresInObj instanceof Integer) {
            return (Integer) expiresInObj;
        } else if (expiresInObj instanceof String) {
            return Integer.parseInt((String) expiresInObj);
        } else if (expiresInObj instanceof Double) {
            return ((Double) expiresInObj).intValue();
        } else {
            throw new RuntimeException("Unexpected type for expires_in");
        }
    }

    /**
     * 기존 회원을 업데이트하거나 신규 회원을 등록함
     *
     * @param kakaoUserInfo 카카오 사용자 정보
     * @param accessToken   액세스 토큰
     * @param refreshToken  리프레시 토큰
     * @param expiresIn     액세스 토큰 만료 시간
     * @return 회원 엔티티
     */
    private Member saveOrUpdateMember(KakaoUserInfo kakaoUserInfo, String accessToken, String refreshToken, int expiresIn) {
        LocalDateTime accessTokenExpiresAt = LocalDateTime.now().plusSeconds(expiresIn);
        LocalDateTime refreshTokenExpiresAt = LocalDateTime.now().plusDays(60);

        Optional<Member> existingMember = memberService.findByKakaoId(kakaoUserInfo.getKakao_Id());
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickname(kakaoUserInfo.getName());
        memberDTO.setKakaoAccessToken(accessToken);
        memberDTO.setKakaoAccessTokenExpiresAt(accessTokenExpiresAt);
        memberDTO.setKakaoRefreshToken(refreshToken);
        memberDTO.setKakaoRefreshTokenExpiresAt(refreshTokenExpiresAt);

        if (existingMember.isPresent()) {
            return memberService.updateMemberProfile(kakaoUserInfo.getKakao_Id(), memberDTO);
        } else {
            memberDTO.setName(kakaoUserInfo.getName());
            memberDTO.setKakao_Id(kakaoUserInfo.getKakao_Id());
            memberDTO.setProfileImage(kakaoUserInfo.getProfileImage());
            return memberService.saveMember(memberDTO);
        }
    }

    /**
     * 쿠키에 JWT 토큰, 리프레시 토큰, 카카오 ID 설정
     *
     * @param jwtToken        JWT 토큰
     * @param refreshTokenJwt 리프레시 토큰
     * @param kakaoId         카카오 ID
     * @return 쿠키가 설정된 HttpHeaders 객체
     */
    private HttpHeaders createCookies(String jwtToken, String refreshTokenJwt, String kakaoId) {
        HttpHeaders headers = new HttpHeaders();
        boolean isSecure = kakaoRedirectUrl.startsWith("https");

        String jwtCookie = "jwtToken=" + jwtToken + "; Path=/; Domain=.slimdealz.store; SameSite=None; Secure";
        String refreshCookie = "refreshToken=" + refreshTokenJwt + "; Path=/; Domain=.slimdealz.store; SameSite=None; Secure";
        String kakaoIdCookie = "kakaoId=" + kakaoId + "; Path=/; Domain=.slimdealz.store; SameSite=None; Secure";

//        String jwtCookie = "jwtToken=" + jwtToken + "; Path=/; SameSite=None; Secure";
//        String refreshCookie = "refreshToken=" + refreshTokenJwt + "; Path=/; SameSite=None; Secure";
//        String kakaoIdCookie = "kakaoId=" + kakaoId + "; Path=/; SameSite=None; Secure";

//        if (isSecure) {
//            jwtCookie += "; Secure";
//            refreshCookie += "; Secure";
//            kakaoIdCookie += "; Secure";
//        }

        headers.add(HttpHeaders.SET_COOKIE, jwtCookie);
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie);
        headers.add(HttpHeaders.SET_COOKIE, kakaoIdCookie);
        return headers;
    }
    /**
     * 카카오 API를 통해 사용자 프로필 정보를 가져옴
     *
     * @param accessToken 액세스 토큰
     * @return 사용자 프로필 정보를 담은 ResponseEntity 객체
     */
    private ResponseEntity<String> getUserProfile(String accessToken) {
        String profileUri = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(profileUri, HttpMethod.GET, entity, String.class);
    }

    @GetMapping("/auth/kakao/logout")
    public ResponseEntity<?> kakaoLogout() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(clientUrl + "/"));  // 클라이언트 홈 페이지로 리디렉트

        // 클라이언트로 리디렉션
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}