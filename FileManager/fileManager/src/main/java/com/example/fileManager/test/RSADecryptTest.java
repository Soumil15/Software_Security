package com.example.fileManager.test;

import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.io.FileReader;
import java.security.PrivateKey;
import java.security.spec.MGF1ParameterSpec;

class RsaDecryptTest {

    private static final String PRIVATE_KEY_PATH = "C:/Users/sarik/JavaSpring/Software_Security/FileManager/private_key/private_key.pem"; // PKCS#8 format

    public static void main(String[] args) throws Exception {
        // The Encrypted AES Key in Base64 from the encryption code
        String encryptedBase64 = "bgWPcJIjQTb0Zw34KrQsGJFzcmNCcKCu9eBr0FtoLTtafDL5tu2U9urCZRbjQCudCR7xUHYPA1mmd8Pyu9632/MVNkUe+mG2Vs9goFlnYVB85iHr/cfqcv6x417EBgfmbizCgAzzRJEfRHiQ1Ga9si/+C0722OkO1lDWMVuPhXVgKmaIm21Ofb7gp1+6LUkO2i8ed1GSu/fATX+PNoa36mmanAH+hN+JNhHFQ1s3j3m+7bVbmtTGgYc4qFVgWHfRfndXP63x92/4kw/f/JkVt+Dm7vYRYC6OxnFsweGoxy1z8sr7JCyFIXkR1o/b0mNt04Xw7nuZNkaWn4UL/gIkdA=="; // Replace with the actual Base64 string

        byte[] encryptedKeyBytes = Base64.decode(encryptedBase64);

        try (PEMParser pemParser = new PEMParser(new FileReader(PRIVATE_KEY_PATH))) {
            Object object = pemParser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

            if (!(object instanceof PrivateKeyInfo)) {
                throw new IllegalArgumentException("Private key is not in the expected format (PKCS#8).");
            }
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) object;
            PrivateKey privateKey = converter.getPrivateKey(privateKeyInfo);

            // Specify OAEP parameters explicitly
            OAEPParameterSpec oaepParams = new OAEPParameterSpec(
                    "SHA-256", // Hash algorithm
                    "MGF1", // Mask generation function
                    MGF1ParameterSpec.SHA256, // MGF1 hash algorithm
                    PSource.PSpecified.DEFAULT // No label
            );

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);

            byte[] decryptedAesKey = cipher.doFinal(encryptedKeyBytes);

            System.out.println("Decrypted AES Key (Hex): " + bytesToHex(decryptedAesKey));
            System.out.println("Decrypted AES Key length: " + decryptedAesKey.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Decryption failed", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}