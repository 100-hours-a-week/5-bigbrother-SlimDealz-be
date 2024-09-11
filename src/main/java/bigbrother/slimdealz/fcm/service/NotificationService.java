package bigbrother.slimdealz.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // Firebase를 통해 푸시 알림을 전송
    public void sendNotification(String token, String title, String message) {
        // 메시지 생성
        Message firebaseMessage = Message.builder()
                .setToken(token) // 수신자 FCM 토큰 설정
                .setNotification(Notification.builder()
                        .setTitle(title)  // 알림 제목
                        .setBody(message)  // 알림 본문
                        .build())
                .build();

        try {
            // 메시지 전송
            String response = FirebaseMessaging.getInstance().send(firebaseMessage);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FCM 메시지 전송 중 오류 발생: " + e.getMessage());
        }
    }
}
