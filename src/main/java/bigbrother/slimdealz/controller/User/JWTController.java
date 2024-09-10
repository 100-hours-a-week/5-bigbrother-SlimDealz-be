package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.auth.JWTConstants;
import bigbrother.slimdealz.auth.JWTutil;
import bigbrother.slimdealz.exception.CustomJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JWTController {

    @RequestMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader(value = "Authorization", required = false) String authHeader, String refreshToken) {
        log.info("Refresh Token = {}", refreshToken);

        if (authHeader == null || !authHeader.startsWith(JWTConstants.JWT_TYPE)) {
            throw new CustomJwtException("유효하지 않은 또는 존재하지 않는 Access Token");
        }

        String accessToken = JWTutil.getTokenFromHeader(authHeader);

        // Access Token 의 만료 여부 확인
        if (!JWTutil.isExpired(accessToken)) {
            return Map.of("Access Token", accessToken, "Refresh Token", refreshToken);
        }

        // refreshToken 검증 후 새로운 토큰 생성 후 전달
        Map<String, Object> claims = JWTutil.validateToken(refreshToken);
        String newAccessToken = JWTutil.generateToken(claims, JWTConstants.ACCESS_EXP_TIME);

        String newRefreshToken = refreshToken;
        long expTime = JWTutil.tokenRemainTime((Integer) claims.get("exp"));
        log.info("Refresh Token Remain Expire Time = {}", expTime);

        if (expTime <= 60) {
            newRefreshToken = JWTutil.generateToken(claims, JWTConstants.REFRESH_EXP_TIME);
        }

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
}
