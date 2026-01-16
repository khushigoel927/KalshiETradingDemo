package com.kg.kalshietrade;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.PSSParameterSpec;
import java.util.Base64;

/**
 * Handles RSA-PSS signing for Kalshi API v2 authentication.
 */
public class KalshiSigner {
    private final PrivateKey privateKey;

    public KalshiSigner(String pemPath) {
        try {
            // 1. Read the file and strip PEM headers/footers/whitespace
            String keyContent = Files.readString(Paths.get(pemPath))
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    //.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    //.replace("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            // 2. Decode from Base64
            byte[] pkcs8EncodedKey = Base64.getDecoder().decode(keyContent);
            // 3. Generate the PrivateKey object (expects PKCS#8 format)
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            this.privateKey = kf.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Failed to load PEM key from " + pemPath
                    + ". Ensure the key is in PKCS#8 format.", e);
        }
    }
    /**
     * Signs the message using RSASSA-PSS as required by Kalshi.
     * Message format: {timestamp}{method}{path}{body}
     */
    public String sign(String message) {
        try {
            // Kalshi v2 requires RSASSA-PSS
            Signature signature = Signature.getInstance("RSASSA-PSS");

            // Define the PSS parameters: SHA-256, MGF1, 32-byte salt
            PSSParameterSpec pssSpec = new PSSParameterSpec(
                    "SHA-256",
                    "MGF1",
                    MGF1ParameterSpec.SHA256,
                    32,
                    1
            );

            signature.setParameter(pssSpec);
            signature.initSign(privateKey);
            signature.update(message.getBytes());
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException("Error generating API signature", e);
        }
    }
}
