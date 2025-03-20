package com.example.fileManager.controller;

import com.example.fileManager.Helper.DBHelper;
import com.example.fileManager.serivce.PwdSerivce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000/")
public class SignUp {

    @PostMapping("/signup")
    public ResponseEntity<String> handleSignUp(@RequestBody FormData formData){

        String username = formData.getUsername();
        String pwd = formData.getPassword();
        String email = formData.getEmail();
        PwdSerivce pwdEncoder = new PwdSerivce();

        DBHelper dbHelper = new DBHelper();

        try{
            if(dbHelper.UserExists(username,email)){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username or email already exists");

            }

            String hashedPwd = pwdEncoder.hashPassword(pwd);
            boolean AddUser = dbHelper.AddNewUser(username,hashedPwd,email);

            if(!AddUser){

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration Failed !");
            }

            return ResponseEntity.ok("User registration was successful");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error, please try again later");
        }


    }

    public static class FormData {
        private String username;  // ✅ Should match frontend
        private String password;  // ✅ Should match frontend
        private String email;

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        @Override
        public String toString() {
            return "FormData{username='" + username + "', email='" + email + "'}";
        }
    }
}
