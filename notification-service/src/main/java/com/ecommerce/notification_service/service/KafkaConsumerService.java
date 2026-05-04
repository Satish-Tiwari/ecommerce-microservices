package com.ecommerce.notification_service.service;

import com.ecommerce.notification_service.constant.KafkaConstant;
import com.ecommerce.notification_service.dto.EmailDetails;
import com.ecommerce.notification_service.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final EmailService emailService;

    @KafkaListener(topics = {KafkaConstant.PROFILE_ONBOARDING_TOPIC, KafkaConstant.USER_LOGOUT_TOPIC}, groupId = "notification-group")
    public void consumeUserEvent(UserEvent event) {
        log.info("Consumed User Event: {}", event);

        if ("USER_CREATED".equals(event.getType())) {
            sendWelcomeEmail(event);
        } else if ("USER_LOGOUT".equals(event.getType())) {
            sendLogoutEmail(event);
        }
    }

    private void sendWelcomeEmail(UserEvent event) {
        String htmlContent = "<html><body style='font-family: Arial, sans-serif;'>" +
                "<div style='background-color: #f4f4f4; padding: 20px; border-radius: 10px;'>" +
                "<div style='background-color: #6200ee; color: white; padding: 10px; border-radius: 5px; text-align: center;'>" +
                "<h1>Welcome to E-Commerce!</h1>" +
                "</div>" +
                "<p style='font-size: 16px;'>Hi <strong>" + event.getFirstName() + "</strong>,</p>" +
                "<p>We are thrilled to have you on board. Start exploring our wide range of products today!</p>" +
                "<div style='text-align: center; margin-top: 20px;'>" +
                "<a href='#' style='background-color: #03dac6; color: black; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Start Shopping</a>" +
                "</div>" +
                "<p style='margin-top: 30px; color: #777; font-size: 12px;'>If you did not create this account, please ignore this email.</p>" +
                "</div></body></html>";

        EmailDetails details = EmailDetails.builder()
                .recipient(event.getEmail())
                .subject("Welcome to our platform!")
                .messageBody(htmlContent)
                .build();

        emailService.sendHtmlMail(details).subscribe(res -> log.info("Welcome email status: {}", res));
    }

    private void sendLogoutEmail(UserEvent event) {
        String htmlContent = "<html><body style='font-family: Arial, sans-serif;'>" +
                "<div style='background-color: #f4f4f4; padding: 20px; border-radius: 10px;'>" +
                "<div style='background-color: #ff5722; color: white; padding: 10px; border-radius: 5px; text-align: center;'>" +
                "<h1>Logout Notification</h1>" +
                "</div>" +
                "<p style='font-size: 16px;'>Hi <strong>" + event.getFirstName() + "</strong>,</p>" +
                "<p>You have been successfully logged out. If this wasn't you, please secure your account immediately.</p>" +
                "<p style='margin-top: 30px; color: #777; font-size: 12px;'>Thank you for using our service.</p>" +
                "</div></body></html>";

        EmailDetails details = EmailDetails.builder()
                .recipient(event.getEmail())
                .subject("Logout Successful")
                .messageBody(htmlContent)
                .build();

        emailService.sendHtmlMail(details).subscribe(res -> log.info("Logout email status: {}", res));
    }
}
