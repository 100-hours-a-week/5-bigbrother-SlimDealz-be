package bigbrother.slimdealz.auth;

import bigbrother.slimdealz.exception.CustomExpiredJwtException;
import bigbrother.slimdealz.exception.CustomJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    // JWT 검증을 적용할 경로 패턴 설정 (필요에 따라 수정 가능)
    private static final String[] protectedPaths = {"/v1/users"};

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        // 보호된 경로가 아닌 경우 필터링하지 않음
        return !isProtectedPath(requestURI);
    }

    private boolean isProtectedPath(String requestURI) {
        for (String path : protectedPaths) {
            if (requestURI.startsWith(path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JWTFilter - 요청 URI: {}", request.getRequestURI());

        String authHeader = request.getHeader(JWTConstants.JWT_HEADER);
        log.info("Authorization Header: {}", authHeader);  // Authorization 헤더 출력

        try {
            if (authHeader != null && authHeader.startsWith(JWTConstants.JWT_TYPE)) {
                // JWT 검증이 필요한 경우
                String token = JWTutil.getTokenFromHeader(authHeader);
                Map<String, Object> claims = JWTutil.validateToken(token);

                log.info("토큰 클레임: {}", claims);

                // 클레임에서 사용자 정보를 가져와서 요청 속성에 설정할 수 있습니다.
                Integer id = (Integer) claims.get("id");
                String kakao_Id = (String) claims.get("kakao_Id");
                String name = (String) claims.get("name");
                String role = (String) claims.get("role");
                String profile_image = (String) claims.get("profile_image");


                log.info("Extracted kakao_Id: {}", kakao_Id);  // kakao_Id 출력

                // 요청에 사용자 정보 속성 추가
                request.setAttribute("id", id);
                request.setAttribute("kakao_Id", kakao_Id);
                request.setAttribute("name", name);
                request.setAttribute("role", role);
                request.setAttribute("profile_image", profile_image);

                // 추가적인 보안 컨텍스트 설정이 필요 없다면 그대로 처리
            } else {
                throw new CustomJwtException("유효하지 않은 토큰입니다");
            }
            filterChain.doFilter(request, response);
        } catch (CustomExpiredJwtException | CustomJwtException e) {
            handleException(response, e);
        }
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        String errorMessage;

        if (e instanceof CustomExpiredJwtException) {
            errorMessage = "Token Expired: " + e.getMessage();
        } else {
            errorMessage = "JWT Error: " + e.getMessage();
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println("{\"error\": \"" + errorMessage + "\"}");
        printWriter.close();
    }
}