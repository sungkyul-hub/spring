package org.skuhub.skuhub.api.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // 인증번호를 저장하는 맵 추가
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();

    // 랜덤으로 숫자 생성
    public String createNumber() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) { // 인증 코드 8자리
            int index = random.nextInt(3); // 0~2까지 랜덤, 랜덤값으로 switch문 실행

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97)); // 소문자
                case 1 -> key.append((char) (random.nextInt(26) + 65)); // 대문자
                case 2 -> key.append(random.nextInt(10)); // 숫자
            }
        }
        return key.toString();
    }

    // 메일 생성 (일반 인증번호용)
    public MimeMessage createMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("[SKUHUB] 이메일 인증");
        String body = "<h3>요청하신 인증 번호입니다.</h3>";
        body += "<h1>" + number + "</h1>";
        body += "<h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");

        return message;
    }

    // 메일 생성 (비밀번호 변경 인증번호용)
    public MimeMessage createPasswordResetMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(senderEmail);
        message.setRecipients(MimeMessage.RecipientType.TO, mail);
        message.setSubject("[비밀번호 변경] 이메일 인증");
        String body = "<h3>요청하신 비밀번호 변경 인증 번호입니다.</h3>";
        body += "<h1>" + number + "</h1>";
        body += "<h3>감사합니다.</h3>";
        message.setText(body, "UTF-8", "html");

        return message;
    }

    // 메일 발송 (일반 인증번호용)
    public String sendSimpleMessage(String sendEmail) throws MessagingException {
        String number = createNumber(); // 랜덤 인증번호 생성

        MimeMessage message = createMail(sendEmail, number); // 메일 생성
        try {
            javaMailSender.send(message); // 메일 발송
            verificationCodes.put(sendEmail, number); // 이메일과 인증번호 저장
        } catch (MailException e) {
            e.printStackTrace();
            throw new MailSendException("메일 발송 중 오류가 발생했습니다."); // 커스텀 예외 추가
        }

        return number; // 생성된 인증번호 반환
    }

    // 비밀번호 변경용 인증번호 발송 메소드
    public String sendPasswordResetEmail(String sendEmail) throws MessagingException {
        String number = createNumber(); // 랜덤 인증번호 생성

        MimeMessage message = createPasswordResetMail(sendEmail, number); // 비밀번호 변경 인증 메일 생성
        try {
            javaMailSender.send(message); // 메일 발송
            verificationCodes.put(sendEmail, number); // 이메일과 인증번호 저장
        } catch (MailException e) {
            e.printStackTrace();
            throw new MailSendException("메일 발송 중 오류가 발생했습니다.");
        }

        return number; // 인증번호 반환
    }

    public boolean verifyCode(String email, String code) {
        return verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code);
    }



    // 커스텀 예외 정의 (선택 사항)
    public static class MailSendException extends RuntimeException {
        public MailSendException(String message) {
            super(message);
        }
    }
}
