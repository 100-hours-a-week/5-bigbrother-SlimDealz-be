package bigbrother.slimdealz.config;

import bigbrother.slimdealz.auth.JWTFilter;
import bigbrother.slimdealz.auth.JwtAuthenticationEntryPoint;
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

import static org.springframework.security.config.Customizer.withDefaults;

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

        corsConfiguration.setAllowedOriginPatterns(List.of("*","http://localhost:5173", "http://localhost:3000", "https://api.slimdealz.store", "https://slimdealz.store"));
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
     * @return SecurityFilterChain 객체
     * @throws Exception 설정 과정에서 발생할 수 있는 예외
     */
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/v1/users").permitAll()
                                .anyRequest().permitAll())
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
//                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(jwtAccessDeniedHandler);
                })
                .addFilterBefore(jwtVerifyFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
