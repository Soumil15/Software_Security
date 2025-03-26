package com.example.fileManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "http://localhost:3000/")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailReceiverService emailReceiverService;

    // ✅ Endpoint to send emails with optional attachment
    @PostMapping("/sendWithAttachment")
    public String sendEmailWithAttachment(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile file) {
        try {
            emailService.sendEmailWithAttachment(to, subject, body, file);
            return "✅ Email sent successfully to " + to;
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to send email: " + e.getMessage();
        }
    }

    // Fetch today's emails
    @GetMapping("/fetchInboxToday")
    public ResponseEntity<List<EmailDTO>> fetchTodayEmails() {
        List<EmailDTO> emails = emailReceiverService.fetchTodayEmails();
        return ResponseEntity.ok(emails);
    }
}
