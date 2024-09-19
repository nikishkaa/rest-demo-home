package org.example.util;

public class SendEmailTLS {

    public static void main(String[] args) {
        MailUtils.sendHTMLMail("gu.nikita.777@gmail.com",
                "HTML", "<b>TEXT<b>", null, null);
    }
}
