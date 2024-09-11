package bigbrother.slimdealz.fcm.controller;

import bigbrother.slimdealz.fcm.entity.FCMMessageRequestDto;
import bigbrother.slimdealz.fcm.service.FirebaseCloudMessageService;
import bigbrother.slimdealz.service.User.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FCMController {

    private final FirebaseCloudMessageService fcmService;
    private final MemberService memberService;

    public FCMController(FirebaseCloudMessageService fcmService, MemberService memberService) {
        this.fcmService = fcmService;
        this.memberService = memberService;
    }

    @PostMapping("/v1/register-fcm-token")
    public ResponseEntity<?> registerFcmToken(@RequestBody FCMMessageRequestDto request) {
        String token = request.getToken();

        // 토큰을 데이터베이스에 저장
        memberService.saveOrUpdateFcmToken(request.getId(), token);

        return ResponseEntity.ok("FCM 토큰이 성공적으로 등록되었습니다.");
    }

    @PostMapping("/v1/send-fcm")
    public ResponseEntity<String> sendFCMMessage(@RequestBody FCMMessageRequestDto requestDto) {
        String response = fcmService.sendMessage(requestDto);
        return ResponseEntity.ok(response);
    }
}
