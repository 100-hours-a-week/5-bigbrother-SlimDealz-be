package bigbrother.slimdealz.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    public static MemberRole fromValue(String value) {
        for (MemberRole role : MemberRole.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
