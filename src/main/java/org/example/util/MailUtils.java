package org.example.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailUtils {

    private final static String USER_NAME = "javaatest@gmail.com";
    private final static String APP_PASSWORD = "fyjs tmct pepn zrgb";

    private static Properties prop = new Properties();

    static {
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
    }

    public static void sendMail(final String to, final String subject, final String msg, final String cc, final String bcc) {
        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USER_NAME, APP_PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USER_NAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
            System.out.println(String.format("Email to %s has been send", to));

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendHTMLMail(final String to, final String subject, final String msg, final String cc, final String bcc) {
        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USER_NAME, APP_PASSWORD);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USER_NAME));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );

            Multipart mp = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(msg, "text/html");
            mp.addBodyPart(htmlPart);
            message.setContent(mp);
            Transport.send(message);
            System.out.println(String.format("HTML Email to %s has been send", to));

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
