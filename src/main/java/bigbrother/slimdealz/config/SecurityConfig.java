package bigbrother.slimdealz.config;

import bigbrother.slimdealz.auth.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * CORS(Cross-Origin Resource Sharing) 설정을 정의하는 메서드입니다.
     * - 모든 도메인("*")으로부터의 요청을 허용합니다.
     * - 허용되는 HTTP 메서드(GET, POST, PUT, DELETE, HEAD, OPTIONS)를 설정합니다.
     * - 허용되는 HTTP 헤더(Authorization, Cache-Control, Content-Type 등)를 설정합니다.
     * - 자격 증명(쿠키, 인증 정보 등)을 함께 보낼 수 있도록 허용합니다.
     *
     * @return CORS 설정이 포함된 CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "baggage", "sentry-trace"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    /**
     * 비밀번호 인코딩에 사용되는 PasswordEncoder를 생성하는 메서드입니다.
     * - BCryptPasswordEncoder를 사용하여 비밀번호를 해시화합니다.
     *
     * @return BCryptPasswordEncoder 객체
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT 인증 필터를 생성하는 메서드입니다.
     * - JWT를 검증하는 필터 객체를 반환합니다.
     *
     * @return JWTFilter 객체
     */
    @Bean
    public JWTFilter jwtVerifyFilter() {
        return new JWTFilter();
    }

    /**
     * Spring Security 필터 체인을 설정하는 메서드입니다.
     * - CORS 설정을 활성화합니다.
     * - CSRF(Cross-Site Request Forgery) 보호를 비활성화합니다.
     * - 세션을 STATELESS로 설정하여 서버에 세션을 저장하지 않도록 구성합니다.
     * - 모든 요청에 대해 인증을 요구하지 않고, 모두 허용합니다.
     *   (코멘트된 부분을 활성화하면 특정 경로에 대해 인증을 요구할 수 있습니다)
     * - UsernamePasswordAuthenticationFilter 이전에 JWT 필터를 추가합니다.
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 설정 과정에서 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS 설정 적용
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

        // CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 세션 관리를 STATELESS로 설정
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> {
            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        // 모든 요청을 인증 없이 허용 (필요 시 특정 경로를 인증 요구로 변경 가능)
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
//                        .requestMatchers("/v1/users/**").authenticated()
                        .anyRequest().permitAll());

        // JWT 필터를 UsernamePasswordAuthenticationFilter 전에 추가
        http.addFilterBefore(jwtVerifyFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
