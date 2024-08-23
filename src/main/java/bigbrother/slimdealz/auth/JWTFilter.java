package bigbrother.slimdealz.auth;

import com.google.gson.Gson;
import bigbrother.slimdealz.exception.CustomExpiredJwtException;
import bigbrother.slimdealz.exception.CustomJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    // JWT 검증을 적용할 경로 패턴 설정
    private static final String[] protectedPaths = {"/api/v1/users/**"};

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return !PatternMatchUtils.simpleMatch(protectedPaths, requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtVerifyFilter - 요청 URI: {}", request.getRequestURI());

        String authHeader = request.getHeader(JWTConstants.JWT_HEADER);

        try {
            if (authHeader != null && authHeader.startsWith(JWTConstants.JWT_TYPE)) {
                // JWT 검증이 필요한 경로일 때만 검증 수행
                String token = JWTutil.getTokenFromHeader(authHeader);
                Authentication authentication = JWTutil.getAuthentication(token);

                log.info("인증 정보: {}", authentication);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleException(response, e);
        }
    }

    private static void checkAuthorizationHeader(String header) {
        if (header == null) {
            throw new CustomJwtException("토큰이 전달되지 않았습니다");
        } else if (!header.startsWith(JWTConstants.JWT_TYPE)) {
            throw new CustomJwtException("BEARER 로 시작하지 않는 올바르지 않은 토큰 형식입니다");
        }
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        Gson gson = new Gson();
        String json;

        if (e instanceof CustomExpiredJwtException) {
            json = gson.toJson(Map.of("Token_Expired", e.getMessage()));
        } else {
            json = gson.toJson(Map.of("error", e.getMessage()));
        }

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(json);
        printWriter.close();
    }
<<<<<<< Updated upstream
}
<<<<<<< Updated upstream
=======
}
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
