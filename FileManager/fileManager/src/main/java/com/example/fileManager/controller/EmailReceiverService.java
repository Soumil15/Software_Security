package com.example.fileManager.controller;

import jakarta.mail.*;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class EmailReceiverService {

    @Value("${spring.mail.imap.host}")
    private String imapHost;

    @Value("${spring.mail.imap.port}")
    private int imapPort;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    public List<EmailDTO> fetchTodayEmails() {
        List<EmailDTO> emailList = new ArrayList<>();
        Store store = null;
        Folder inbox = null;

        try {
            // IMAP Configuration
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imap");
            properties.put("mail.imap.host", imapHost);
            properties.put("mail.imap.port", String.valueOf(imapPort));
            properties.put("mail.imap.auth", "true");
            properties.put("mail.imap.ssl.enable", "true");

            // Create IMAP Session
            Session session = Session.getInstance(properties);
            store = session.getStore("imap");
            store.connect(imapHost, username, password);

            // Open Inbox
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Fetch the latest 20 emails
            Message[] messages = inbox.getMessages(Math.max(1, inbox.getMessageCount() - 20), inbox.getMessageCount());

            for (int i = messages.length - 1; i >= 0; i--) {
    Message message = messages[i];
    if (isEmailFromToday(message)) {
        emailList.add(new EmailDTO(
            message.getSubject(),
            message.getFrom()[0].toString(),
            message.getSentDate(),
            getTextFromMessage(message)
        ));
    }
}

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inbox != null) inbox.close(false);
                if (store != null) store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        return emailList;
    }

    private boolean isEmailFromToday(Message message) {
        try {
            return message.getReceivedDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                    .equals(java.time.LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }

    private String getTextFromMessage(Message message) throws Exception {
    if (message.isMimeType("text/plain")) {
        return message.getContent().toString();
    } else if (message.isMimeType("text/html")) {
        return Jsoup.parse(message.getContent().toString()).text();
    } else if (message.isMimeType("multipart/*")) {
        return getTextFromMultipart((Multipart) message.getContent());
    }
    return "No readable content";
}


    private String getTextFromMultipart(Multipart multipart) throws Exception {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.isMimeType("text/plain")) {
                result.append(part.getContent().toString()).append("\n");
            } else if (part.isMimeType("text/html")) {
                result.append(Jsoup.parse(part.getContent().toString()).text()).append("\n");
            } else if (part.getContent() instanceof Multipart) {
                result.append(getTextFromMultipart((Multipart) part.getContent()));
            }
        }
        return result.toString().trim();
    }
}
