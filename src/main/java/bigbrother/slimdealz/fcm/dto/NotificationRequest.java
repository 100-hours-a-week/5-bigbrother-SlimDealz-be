package bigbrother.slimdealz.fcm.dto;

public class NotificationRequest {
    private String fcmToken; // FCM 토큰
    private String title;     // 알림 제목
    private String message;   // 알림 메시지

    // Getter 및 Setter
    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
