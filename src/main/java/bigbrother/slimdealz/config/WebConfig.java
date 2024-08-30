package bigbrother.slimdealz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
<<<<<<< HEAD
                .allowedOrigins("${CLIENT_SERVER}")
=======
                .allowedOrigins(clientURL, "https://slimdealz.store")
>>>>>>> upstream/develop
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")  // 필요 시 노출할 헤더
                .allowCredentials(true);
    }
}