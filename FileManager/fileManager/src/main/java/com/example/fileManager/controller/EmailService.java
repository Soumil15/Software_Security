package com.example.fileManager.controller;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private int smtpPort;

    @Value("${spring.mail.username}")
    private String smtpUsername;

    @Value("${spring.mail.password}")
    private String smtpPassword;

    public void sendEmailWithAttachment(String to, String subject, String body, MultipartFile file) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", String.valueOf(smtpPort));
        properties.put("mail.smtp.ssl.trust", smtpHost);

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpUsername));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        Multipart multipart = new MimeMultipart();

        //Add email body
        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setText(body);
        multipart.addBodyPart(textBodyPart);

        //If file exists, add as an attachment
        if (file != null && !file.isEmpty()) {
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            File tempFile = convertMultiPartToFile(file);
            attachmentBodyPart.attachFile(tempFile);
            attachmentBodyPart.setFileName(file.getOriginalFilename());
            multipart.addBodyPart(attachmentBodyPart);
        }

        message.setContent(multipart);
        Transport.send(message);
    }

    private File convertMultiPartToFile(MultipartFile file) throws Exception {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
