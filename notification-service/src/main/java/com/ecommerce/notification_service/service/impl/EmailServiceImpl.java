package com.ecommerce.notification_service.service.impl;

import com.ecommerce.notification_service.service.EmailService;
import com.ecommerce.notification_service.dto.EmailDetails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public Mono<String> sendSimpleMail(EmailDetails details) {
        return Mono.fromCallable(() -> {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMessageBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }).onErrorResume(ex -> {
            log.error("Error while sending mail", ex);
            return Mono.just("Error while Sending Mail");
        });
    }

    @Override
    public Mono<String> sendMailWithAttachment(EmailDetails details) {
        return Mono.fromCallable(() -> {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setText(details.getMessageBody());
            mimeMessageHelper.setSubject(details.getSubject());

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully...";
        }).onErrorResume(ex -> {
            log.error("Error while sending mail with attachment", ex);
            return Mono.just("Error while Sending Mail with attachment");
        });
    }

    @Override
    public Mono<String> sendMail(MultipartFile[] files, String to, String[] cc, String subject, String body) {
        return Mono.fromCallable(() -> {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            for (MultipartFile file : files) {
                mimeMessageHelper.addAttachment(
                        Objects.requireNonNull(file.getOriginalFilename()),
                        new ByteArrayResource(file.getBytes()));
            }

            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully...";
        }).onErrorResume(ex -> {
            log.error("Error while sending mail", ex);
            return Mono.just("Error while Sending Mail");
        });
    }

    @Override
    public Mono<String> sendHtmlMail(EmailDetails details) {
        return Mono.fromCallable(() -> {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setSubject(details.getSubject());
            mimeMessageHelper.setText(details.getMessageBody(), true);

            javaMailSender.send(mimeMessage);
            return "HTML Mail Sent Successfully...";
        }).onErrorResume(ex -> {
            log.error("Error while sending HTML mail", ex);
            return Mono.just("Error while Sending HTML Mail");
        });
    }
}
