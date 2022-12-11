package com.dambae200.dambae200.domain.user.service;

import com.dambae200.dambae200.global.error.exception.BusinessException;
import com.dambae200.dambae200.global.error.exception.ErrorCode;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

public class PasswordSender {

    static public void send(String newPw, String recipient){

        // 1. 발신자의 메일 계정과 비밀번호 설정
        final String user = "dambae200@gmail.com";
        final String password = "ayvfgmtoodwnxmfx";

        // 2. Property에 SMTP 서버 정보 설정
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");


//        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

        // 3. SMTP 서버정보와 사용자 정보를 기반으로 Session 클래스의 인스턴스 생성
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });


        // 4. Message 클래스의 객체를 사용하여 수신자와 내용, 제목의 메시지를 작성한다.
        // 5. Transport 클래스를 사용하여 작성한 메세지를 전달한다.

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(user));


        // 수신자 메일 주소
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

        // Subject
        message.setSubject("담배200 임시 비밀번호입니다");

        // Text
        message.setText("안녕하세요 담배200입니다. 아래 비밀번호를 임시로 사용해주시고, 꼭 변경해주세요 !\n\n" + newPw);

        Transport.send(message);    // send message
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "이메일 전송에 문제가 생겼습니다.");
        }
    }




}
