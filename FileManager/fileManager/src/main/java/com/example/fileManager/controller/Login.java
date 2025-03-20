package com.example.fileManager.controller;

import com.example.fileManager.Helper.DBHelper;
import com.example.fileManager.serivce.PwdSerivce;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000/")
public class Login {

    @PostMapping("/login")
    public ResponseEntity<String> handleLogin(@RequestBody LoginData loginData, HttpSession session){

        String username = loginData.username;
        String password = loginData.password;
        PwdSerivce pwdSerivce = new PwdSerivce();
        DBHelper dbHelper =  new DBHelper();

        String hashedPwd = dbHelper.hashedPwd(username);


        if(!pwdSerivce.verifyPassword(password,hashedPwd)){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        session.setAttribute("username", username);


        return ResponseEntity.ok("Login successful");
    }

    public static class LoginData{

        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }


        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
