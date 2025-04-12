package com.mobile.api.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for generate code.
 */
public final class CodeGeneratorUtils {
    private static final SecureRandom random = new SecureRandom();

    public CodeGeneratorUtils() {
        // Prevent instantiation
    }

    /**
     * Generate code with specified length.
     */
    public static String generateAlphanumericCode(Integer length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Generate OTP with specified length.
     */
    public static String generateOTPCode(Integer length) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    /**
     * Generate code verifier for PKCE.
     */
    public static String generateCodeVerifier() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
        SecureRandom sr = new SecureRandom();
        StringBuilder sb = new StringBuilder(128); // Max 128 chars
        for (int i = 0; i < 64; i++) { // thường 43–128 ký tự
            int index = sr.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Generate code challenge from code verifier.
     */
    public static String generateCodeChallenge(String codeVerifier) {
        try {
            byte[] bytes = codeVerifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(bytes);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available");
        }
    }
}
