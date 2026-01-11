package com.kg.kalshietrade;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class KalshiSigner {
    private final PrivateKey privateKey;
    public KalshiSigner(String pemPath) {
        try {
            String pem = Files.readString(Paths.get(pemPath))
                    .replaceAll("\\r?\\n", "")
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "");
            byte[] decoded = Base64.getDecoder().decode(pem);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
            privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }

    public String sign(String message) {
        try {
            Signature sig = Signature.getInstance("RSASSA-PSS");
            sig.setParameter(new PSSParameterSpec(
                    "SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1));
            sig.initSign(privateKey);
            sig.update(message.getBytes());
            return Base64.getEncoder().encodeToString(sig.sign());
        } catch (Exception e) {
            throw new RuntimeException("Signing failed", e);
        }
    }

}
