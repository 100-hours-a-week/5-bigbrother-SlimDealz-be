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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
public class KakaoAuthController {

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;

    @Value("${SERVER_URL}/auth/kakao/callback")
    private String kakaoRedirectUrl;

    @Value("${CLIENT_URL}")
    private String client_Url;

    @Autowired
    private final MemberService memberService;
    private final RestTemplate restTemplate;

    public KakaoAuthController(MemberService memberService, RestTemplate restTemplate) {
        this.memberService = memberService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> handleKakaoCallback(@RequestParam("code") String code) {
        // 1. 카카오 서버에 액세스 토큰 요청
        Map<String, Object> tokens = getKakaoAccessToken(code);

        String accessToken = (String) tokens.get("access_token");
        String refreshToken = (String) tokens.get("refresh_token");

        // expires_in 값이 String 타입으로 반환될 수 있으므로, 이를 Integer로 변환
        Integer expiresIn = null;
        if (tokens.get("expires_in") instanceof Integer) {
            expiresIn = (Integer) tokens.get("expires_in");
        } else if (tokens.get("expires_in") instanceof String) {
            expiresIn = Integer.parseInt((String) tokens.get("expires_in"));
        } else if (tokens.get("expires_in") instanceof Double) {
            expiresIn = ((Double) tokens.get("expires_in")).intValue();
        } else {
            throw new RuntimeException("Unexpected type for expires_in");
        }

        LocalDateTime accessTokenExpiresAt = LocalDateTime.now().plusSeconds(expiresIn);
        LocalDateTime refreshTokenExpiresAt = LocalDateTime.now().plusDays(60); // 예: 60일 만료

        // 2. 액세스 토큰을 사용하여 사용자 프로필 정보 가져오기
        ResponseEntity<String> userProfileResponse = getUserProfile(accessToken);
        Map<String, Object> userProfile = new Gson().fromJson(userProfileResponse.getBody(), Map.class);

        // 3. KakaoUserInfo 객체로 사용자 정보 추출
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(userProfile);

        // 4. 기존 회원 확인 및 등록 또는 업데이트
        Optional<Member> existingMember = memberService.findByKakaoId(kakaoUserInfo.getKakao_Id());
        Member member;

        if (existingMember.isPresent()) {
            // 기존 회원 업데이트
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setNickname(kakaoUserInfo.getName());
            memberDTO.setKakaoAccessToken(accessToken);
            memberDTO.setKakaoAccessTokenExpiresAt(accessTokenExpiresAt);
            memberDTO.setKakaoRefreshToken(refreshToken);
            memberDTO.setKakaoRefreshTokenExpiresAt(refreshTokenExpiresAt);

            member = memberService.updateMemberProfile(kakaoUserInfo.getKakao_Id(), memberDTO);
        } else {
            // 신규 회원 등록
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setName(kakaoUserInfo.getName());
            memberDTO.setKakao_Id(kakaoUserInfo.getKakao_Id());
            memberDTO.setProfileImage(kakaoUserInfo.getProfileImage());
            memberDTO.setKakaoAccessToken(accessToken);
            memberDTO.setKakaoAccessTokenExpiresAt(accessTokenExpiresAt);
            memberDTO.setKakaoRefreshToken(refreshToken);
            memberDTO.setKakaoRefreshTokenExpiresAt(refreshTokenExpiresAt);

            member = memberService.saveMember(memberDTO);
        }

        // JWT 토큰 생성 및 리디렉션
        Map<String, Object> claims = Map.of(
                "kakao_Id", member.getKakao_Id(),
                "name", member.getName(),
                "role", member.getRole().getValue(),
                "profile_image", member.getProfileImage()
        );

        String jwtToken = JWTutil.generateToken(claims, JWTConstants.ACCESS_EXP_TIME);
        String refreshTokenJwt = JWTutil.generateToken(claims, JWTConstants.REFRESH_EXP_TIME);

        String redirectUrl = client_Url + "/?jwtToken=" + URLEncoder.encode(jwtToken, StandardCharsets.UTF_8) +
                "&refreshToken=" + URLEncoder.encode(refreshTokenJwt, StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
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