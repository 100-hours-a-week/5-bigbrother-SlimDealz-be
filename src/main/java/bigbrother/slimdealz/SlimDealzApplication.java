package bigbrother.slimdealz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SlimDealzApplication {

    public static void main(String[] args) {
        SpringApplication.run(SlimDealzApplication.class, args);
    }

}
