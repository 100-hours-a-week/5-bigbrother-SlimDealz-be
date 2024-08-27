package bigbrother.slimdealz.controller.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    @Value("${SERVER_URL}/oauth2/authorization/kakao")
    private String kakaoAuthUrl;

    @GetMapping("/kakao-url")
    public ResponseEntity<String> getKakaoAuthUrl() {
        return ResponseEntity.ok(kakaoAuthUrl);
    }
}
