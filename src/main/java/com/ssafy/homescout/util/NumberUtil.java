package com.ssafy.homescout.util;

import java.security.SecureRandom;

public class NumberUtil {
    public static String generateAuthCode() {
        SecureRandom random = new SecureRandom();
        // 100000 ~ 999999 범위의 난수 생성
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
