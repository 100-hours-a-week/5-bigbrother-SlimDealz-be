package bigbrother.slimdealz.controller.User;

import bigbrother.slimdealz.auth.JWTConstants;
import bigbrother.slimdealz.auth.JWTutil;
import bigbrother.slimdealz.exception.CustomJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JWTController {

    @PostMapping("/refresh")
    public Map<String, Object> refresh(
            @RequestParam(value = "refreshToken") String refreshToken) {

        log.info("Received Refresh Token: {}", refreshToken);

        try {
            // Refresh Token 검증
            Map<String, Object> claims = JWTutil.validateToken(refreshToken);

            // 새로운 Access Token 생성
            String newAccessToken = JWTutil.generateToken(claims, JWTConstants.ACCESS_EXP_TIME);

            // Refresh Token의 남은 유효 시간 확인
            long refreshTokenExpiryTime = JWTutil.tokenRemainTime(((Integer) claims.get("exp")));
            String newRefreshToken = refreshToken;

            // Refresh Token의 남은 시간이 60초 이하이면 새로 발급
            if (refreshTokenExpiryTime <= 60) {
                newRefreshToken = JWTutil.generateToken(claims, JWTConstants.REFRESH_EXP_TIME);
                log.info("Refresh Token has been regenerated due to short expiration time.");
            }

            log.info("New Access Token generated: {}", newAccessToken);
            log.info("New Refresh Token: {}", newRefreshToken);

            // 새로 발급된 Access Token과 Refresh Token 반환
            return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);

        } catch (CustomJwtException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            throw new CustomJwtException("Refresh Token validation failed.");
        } catch (Exception e) {
            log.error("An unexpected error occurred during token refresh: {}", e.getMessage());
            throw new RuntimeException("An error occurred while refreshing the token.");
        }
    }
}