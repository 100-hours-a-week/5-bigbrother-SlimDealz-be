//package bigbrother.slimdealz.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//
//@Configuration
//public class RedisConfig {
//    @Value("${REDIS_HOST}")
//    private String redisHost;
//
//    @Value("${REDIS_PORT}")
//    private int redisPort;
//
//    @Value("${REDIS_PASSWORD}")
//    private String redisPassword;
//
//    // Redis 서버 연결
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
//        config.setPassword(redisPassword);
//        return new LettuceConnectionFactory(config);
//    }
//
//    // 데이터 저장, 조회 템플릿 클래스
//    @Bean
//    public RedisTemplate<?,?> redisTemplate() {
//        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//        return redisTemplate;
//    }
//}
