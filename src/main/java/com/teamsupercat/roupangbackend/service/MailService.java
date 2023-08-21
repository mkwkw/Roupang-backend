package com.teamsupercat.roupangbackend.service;


import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.entity.EmailVerification;
import com.teamsupercat.roupangbackend.repository.EmailVerificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "supercatstore3@gmail.com";
    private static Integer number;

    private final EmailVerificationRepository emailVerificationRepository;



    public Integer createNumber() {
        return number = (int) (Math.random() * (90000)) + 100000;
    }

    public MimeMessage CreateMail(String email) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            message.setSubject("이메일 인증");
            log.info(email);
            String body = "<html><head></head><body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4;\"><div style=\"max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border-radius: 5px; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\"><div style=\"background-color: #007bff; color: #fff; text-align: center; padding: 10px;\"><h1>메일 인증</h1></div><p style=\"padding: 20px;\">안녕하세요! 아래의 인증 번호를 사용하여 이메일 인증을 완료해주세요.</p><h2 style=\"text-align: center; padding: 20px;\">인증 번호: " + number + "</h2><p style=\"padding: 20px;\">감사합니다.</p></div></body></html>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public ResponseDto<?> sendEmail(String email) {
        try {
            MimeMessage message = CreateMail(email);

            javaMailSender.send(message);

            LocalDateTime expireAt = LocalDateTime.now().plusMinutes(5);

            EmailVerification emailVerification1 = new EmailVerification(email, number, expireAt);

            emailVerificationRepository.save(emailVerification1);

            return ResponseDto.success("이메일 전송에 성공하였습니다");
        }catch (CustomException e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.EMAIL_VERIFICATION_SEND_FAILED);
        }
    }

    public ResponseDto<?> verificationCode(String email, Integer code){

        EmailVerification emailVerification1 = emailVerificationRepository.findByVerificationCode(code)
                .orElseThrow(()-> new CustomException(ErrorCode.EMAIL_VERIFICATION_CODE_FAILED));

        LocalDateTime expire = emailVerification1.getExpiresAt();

        if (email.isEmpty() || emailVerification1.getEmail().matches(email)) {
                throw new CustomException(ErrorCode.EMAIL_VERIFICATION_NOT_FOUND_USER);
            }

        if (expire.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.EMAIL_VERIFICATION_EXPIRED_DATE);
        }

        return ResponseDto.success("인증에 성공하였습니다");
        }
}