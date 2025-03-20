package com.example.fileManager.controller;


import com.example.fileManager.Helper.DBHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;
import java.util.Base64;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000/")
public class upload {

    // NOTE !!! : UPLOAD_DIR should be the same for both the upload and download endpoint.
    private static final String UPLOAD_DIR = ""; // PLEASE ADD THE DIRECTORY OF YOUR CHOICE

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Generate a 32-byte (256-bit) AES key
            byte[] aesKey = generateAesKey();

            // Read the file data
            byte[] fileData = file.getBytes();

            // Encrypt the file using AES-256 (ECB mode)
            byte[] encryptedData = encryptFile(fileData, aesKey);


            // Save the encrypted file
            File encryptedFile = new File(UPLOAD_DIR + file.getOriginalFilename() + ".enc");
            try (FileOutputStream fos = new FileOutputStream(encryptedFile)) {
                fos.write(encryptedData);
            }

            String encodedKey = Base64.getEncoder().encodeToString(aesKey).trim();


            DBHelper dbHelper = new DBHelper();
            dbHelper.saveKeyToDatabase(file.getOriginalFilename(),encodedKey);

            return ResponseEntity.ok("File uploaded and encrypted successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File encryption failed: " + e.getMessage());
        }
    }

    private byte[] generateAesKey() {
        // Generate a 32-byte (256-bit) AES key
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[32];
        secureRandom.nextBytes(key);
        return key;
    }

    private byte[] encryptFile(byte[] fileData, byte[] aesKey) throws Exception {
        // Validate AES key length
        if (aesKey.length != 32) {
            throw new IllegalArgumentException("Invalid AES key length! Expected 32 bytes for AES-256, but got " + aesKey.length);
        }

        // Encrypt using AES-256 (ECB mode)
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Use ECB mode with PKCS5 padding
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return cipher.doFinal(fileData);
    }
}


