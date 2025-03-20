package com.example.fileManager.serivce;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

public class PwdSerivce {

    private final BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();

    public String hashPassword(String plainPwd){

        // Generate a secure hash using bcrypt

        return pwdEncoder.encode(plainPwd);
    }

    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        //Compares the entered password with the stored hash
        return pwdEncoder.matches(rawPassword, hashedPassword);
    }


}
