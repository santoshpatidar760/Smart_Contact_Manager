package com.smart.smartContactManager.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailService {

    public boolean sendEmail(String subject, String message, String to) {
        boolean flag = false;

        // Sender's email
        String from = "mahajantanu0003@gmail.com"; // Replace with your email
        String password = "msuunqaasqudmtjv"; // Replace with your app-specific password

        // SMTP properties for Gmail
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Authenticator for the session
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Creating message
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
//            mimeMessage.setText(message);
            mimeMessage.setContent(message,"text/html");

            // Sending message
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully...");
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }
}
