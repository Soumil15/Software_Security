package com.example.fileManager.controller;

import com.example.fileManager.Helper.DBHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000/")
public class download {

    // NOTE !!! : UPLOAD_DIR should be the same for both the upload and download endpoint.
    private static final String UPLOAD_DIR = ""; // PLEASE ADD THE DIRECTORY OF YOUR CHOICE

    @GetMapping("/downloads")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String fileName){

        System.out.println("Download request received for: " + fileName);


        //First check if the file exists
        DBHelper dbHelper = new DBHelper();
        boolean check = dbHelper.checkIfFileExists(fileName);

        if(!check){

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("No such file exists: " + fileName).getBytes());
        }
        try{
            String retrievedKey = dbHelper.retrieveKey(fileName);
            //The AES stored in the db as Base64 encoded. So it needs to be decoded
            byte[] aesKey = Base64.getDecoder().decode(retrievedKey);

            // Read the encrypted file from the server's designated folder
            File encryptedFile = new File(UPLOAD_DIR + "/" + fileName + ".enc");
            if (!encryptedFile.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(("Encrypted file not found on server: " + fileName).getBytes());
            }

            byte[] encryptedData = Files.readAllBytes(encryptedFile.toPath());
            byte[] decryptedData = decryptFile(encryptedData, aesKey);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(decryptedData);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error during file decryption: " + e.getMessage()).getBytes());
        }

    }

    private byte[] decryptFile(byte[] encryptedData, byte[] aesKey) throws Exception {
        // Validate AES key length
        if (aesKey.length != 32) {
            throw new IllegalArgumentException("Invalid AES key length! Expected 32 bytes for AES-256, but got " + aesKey.length);
        }

        // Decrypt using AES-256 (ECB mode)
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Use ECB mode with PKCS5 padding
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        return cipher.doFinal(encryptedData); // Pass the encrypted file data here
    }

}
