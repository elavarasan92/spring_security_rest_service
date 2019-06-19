package com.questionbank.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailTLS {

    public static void sendOTP(String toEmail,String otp) {

    	System.out.println("Email and otp : "+toEmail+" : "+otp);
        final String username = "rameshelava@gmail.com";
        final String password = "elavarasan1";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("rameshelava@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("elavarasan_natarajan@yahoo.com,"+toEmail)
            );
            message.setSubject("Otp from QuestionBank");
            message.setText("Dear user,"
                    + "\n\n Please find the otp : "+otp);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    /*public static void main(String [] args) {
    	SendEmailTLS.sendOTP("rameshelava@gmail.com","1234");
    }*/

}