package bigbrother.slimdealz.entity;

import java.util.Map;

public class KakaoUserInfo {

    private final String socialId;
    private final Map<String, Object> profile;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.socialId = String.valueOf(attributes.get("id"));
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        this.profile = (Map<String, Object>) account.get("profile");
    }

    public String getSocialId() {
        return socialId;
    }

    public String getName() {

        return String.valueOf(profile.get("nickname"));
    }

    public String getProfileImage() {
        return String.valueOf(profile.get("profile_image_url"));
    }
}
