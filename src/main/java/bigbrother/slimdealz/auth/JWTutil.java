package bigbrother.slimdealz.auth;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.entity.MemberRole;
import bigbrother.slimdealz.exception.CustomExpiredJwtException;
import bigbrother.slimdealz.exception.CustomJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class JWTutil {

    // 비밀키는 환경 변수 또는 설정 파일에서 관리하는 것이 좋습니다.
    public static String secretKey = JWTConstants.key;

    // 헤더에 "Bearer XXX" 형식으로 담겨온 토큰을 추출
    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    // JWT 생성
    public static String generateToken(Map<String, Object> claims, int validTime) {
        SecretKey key = Keys.hmacShaKeyFor(JWTutil.secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validTime * 60 * 1000))  // 유효 시간 설정 (분 단위)
                .signWith(key)
                .compact();
    }

    // JWT 검증 및 클레임 추출
    public static Map<String, Object> validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(JWTutil.secretKey.getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomExpiredJwtException("토큰이 만료되었습니다", expiredJwtException);
        } catch (Exception e) {
            throw new CustomJwtException("유효하지 않은 토큰입니다");
        }
    }

    // JWT로부터 Authentication 객체 생성
    public static Authentication getAuthentication(String token) {
        Map<String, Object> claims = validateToken(token);

        // kakao_Id와 name, role 정보를 사용
        String kakao_Id = (String) claims.get("kakao_Id");
        String name = (String) claims.get("name");
        String role = (String) claims.get("role");
        MemberRole memberRole = MemberRole.fromValue(role);

        Member member = Member.builder().kakao_Id(kakao_Id).name(name).role(memberRole).build();
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(member.getRole().getValue()));
        PrincipalDetail principalDetail = new PrincipalDetail(member, authorities);

        return new UsernamePasswordAuthenticationToken(principalDetail, "", authorities);
    }

    // 토큰이 만료되었는지 확인
    public static boolean isExpired(String token) {
        try {
            validateToken(token);
            return false;
        } catch (CustomExpiredJwtException e) {
            return true;
        }
    }

    // 토큰의 남은 만료 시간 계산
    public static long tokenRemainTime(Integer expTime) {
        Date expDate = new Date((long) expTime * 1000);
        long remainMs = expDate.getTime() - System.currentTimeMillis();
        return remainMs / (1000 * 60);
    }
}
