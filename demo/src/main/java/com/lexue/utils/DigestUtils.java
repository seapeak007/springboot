package com.lexue.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by danielmiao on 2017/5/5.
 * Version: 1.0.0
 */
public class DigestUtils {

    /**
     * Md 5 digest byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public static final byte[] MD5Digest(byte[] data) {
        return MessageDigest(data, "MD5");
    }

    /**
     * Sha 1 digest byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public static final byte[] SHA1Digest(byte[] data) {
        return MessageDigest(data, "SHA-1");
    }

    /**
     * Sha 256 digest byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    public static final byte[] SHA256Digest(byte[] data) {
        return MessageDigest(data, "SHA-256");
    }

    private static final byte[] MessageDigest(byte[] data, String algorithms) {
        byte[] result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithms);
            digest.update(data);
            result = digest.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        return result;
    }

    public static byte[] encryptHMAC(byte[] data, byte[] key) {
        byte[] result = null;
        SecretKey secretKey = new SecretKeySpec(key, "HmacMD5");
        Mac mac;
        try {
            mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            result = mac.doFinal(data);
        } catch (Exception e) {
        }
        return result;
    }
}