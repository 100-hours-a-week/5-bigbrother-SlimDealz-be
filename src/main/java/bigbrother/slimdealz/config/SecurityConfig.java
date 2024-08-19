package bigbrother.slimdealz.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers("/signup/**").authenticated();
                    authorizeRequests.requestMatchers("signin/**").authenticated();
                    authorizeRequests.anyRequest().permitAll();
                })
                .formLogin((formLogin) -> {
                    formLogin.loginPage("/signin");
                })
                .build();
    }
}
