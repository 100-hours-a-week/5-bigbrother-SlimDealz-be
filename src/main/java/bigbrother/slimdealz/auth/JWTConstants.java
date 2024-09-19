package bigbrother.slimdealz.auth;

import org.springframework.beans.factory.annotation.Value;

public class JWTConstants {

    public static final String key = "DG3K2NG9lK3T2FLfnO283HO1NFLAy9FGJ23UM9Rv923YRV923HT";

    public static final int ACCESS_EXP_TIME = 1;   // 30분
    public static final int REFRESH_EXP_TIME = 60 * 24 * 30;   // 30일

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_TYPE = "Bearer ";
}
