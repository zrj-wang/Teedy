package com.sismics.util;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.sismics.util.totp.GoogleAuthenticator;
import com.sismics.util.totp.GoogleAuthenticatorKey;

/**
 * Enhanced test of {@link GoogleAuthenticator} to improve instruction coverage
 */
public class Test1 {
    @Test
    public void testGoogleAuthenticator() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        // 原有断言：验证是否生成
        Assert.assertNotNull(key.getVerificationCode());
        Assert.assertEquals(5, key.getScratchCodes().size());

        // ✅ 新增指令覆盖：访问 key 相关字段和方法
        Assert.assertNotNull("Key should not be null", key.getKey());
        System.out.println("Key (string): " + key.getKey());
        System.out.println("Key (toString): " + key.toString());

        // ✅ 遍历 scratchCodes 列表
        for (Integer scratchCode : key.getScratchCodes()) {
            Assert.assertTrue("Scratch code should be positive", scratchCode > 0);
        }

        // 原有验证逻辑
        int validationCode = gAuth.calculateCode(key.getKey(), new Date().getTime() / 30000);
        Assert.assertTrue(gAuth.authorize(key.getKey(), validationCode));

        int wrongCode = (validationCode + 1) % 1000000;
        Assert.assertFalse("Wrong code should not pass", gAuth.authorize(key.getKey(), wrongCode));
    }
}
