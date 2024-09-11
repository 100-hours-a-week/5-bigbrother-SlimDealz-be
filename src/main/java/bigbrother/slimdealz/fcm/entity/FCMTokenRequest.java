package bigbrother.slimdealz.fcm.entity;

public class FCMTokenRequest {
    private String token; // 클라이언트에서 전송한 FCM 토큰

    // Getter 및 Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
