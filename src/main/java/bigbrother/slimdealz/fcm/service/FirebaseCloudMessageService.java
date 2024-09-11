package bigbrother.slimdealz.fcm.service;

import bigbrother.slimdealz.entity.Member;
import bigbrother.slimdealz.fcm.entity.FCMMessageRequestDto;
import bigbrother.slimdealz.service.User.MemberService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class FirebaseCloudMessageService {

    private final MemberService memberService;

    public FirebaseCloudMessageService(MemberService memberService) {
        this.memberService = memberService;
    }

    public String sendMessage(FCMMessageRequestDto requestDto) {
        // 사용자 ID로 FCM 토큰 조회
        Member member = memberService.findMemberById(requestDto.getId());

        // FCM 토큰이 존재하지 않을 경우 처리
        if (member.getFcmToken() == null || member.getFcmToken().isEmpty()) {
            return "User does not have a valid FCM token.";
        }

        // 메시지 구성
        Message message = Message.builder()
                .putData("title", requestDto.getTitle())
                .putData("body", requestDto.getBody())
                .setToken(member.getFcmToken()) // FCM 토큰 사용
                .build();

        try {
            // 메시지 전송
            String response = FirebaseMessaging.getInstance().send(message);
            return "Message sent successfully: " + response;
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Failed to send message";
        }
    }
}
