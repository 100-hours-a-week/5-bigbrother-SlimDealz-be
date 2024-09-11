package bigbrother.slimdealz.fcm.controller;

import bigbrother.slimdealz.fcm.entity.FCMTokenRequest;
import bigbrother.slimdealz.fcm.service.FCMService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users/fcm")
public class FCMController {

    private final FCMService fcmService;

    public FCMController(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    // 클라이언트에서 FCM 토큰을 받아 저장하는 엔드포인트
    @PostMapping("/register")
    public ResponseEntity<String> registerFCMToken(@RequestBody FCMTokenRequest fcmTokenRequest,
                                                   HttpServletRequest request) {
        // JWT 필터에서 설정된 Id, name 등의 정보를 가져옴
        Integer id = (Integer) request.getAttribute("id");
        String name = (String) request.getAttribute("name");

        // FCM 토큰 저장 또는 업데이트
        fcmService.saveOrUpdateToken(id.longValue(), fcmTokenRequest.getToken());

        return ResponseEntity.ok("FCM 토큰이 성공적으로 저장되었습니다.");
    }
}
