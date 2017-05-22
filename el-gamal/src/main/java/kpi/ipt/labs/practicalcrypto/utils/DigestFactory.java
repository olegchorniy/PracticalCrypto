package kpi.ipt.labs.practicalcrypto.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class DigestFactory {

    private DigestFactory() {
    }

    public static MessageDigest createMD5() {
        return createDigestInstance("MD5");
    }

    public static MessageDigest createSHA1() {
        return createDigestInstance("SHA-1");
    }

    public static MessageDigest createDigestInstance(String algorithmName) {
        try {
            return MessageDigest.getInstance(algorithmName);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unknown message digest algorithm: " + algorithmName, e);
        }
    }
}