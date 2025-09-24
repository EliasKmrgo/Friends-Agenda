package com.friendsagenda.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility to compute canonical identifiers.
 * <p>
 * Exposes a helper to compute a SHA-256 hash in 64-char lower-case hex form.
 */
public final class CanonicalId {

    private CanonicalId() {
        // Utility class
    }

    /**
     * Computes SHA-256 over the given input and returns a 64-character
     * lower-case hexadecimal string.
     *
     * @param input input text to hash (non-null)
     * @return 64-char hex string
     * @throws IllegalStateException if SHA-256 algorithm is unavailable
     */
    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}

