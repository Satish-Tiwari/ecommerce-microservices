package com.ecommerce.notification_service.dto;

import org.springframework.web.multipart.MultipartFile;

public class EmailSender {
    MultipartFile[] file;
    String to;
    String[] cc;
    String subject;
    String body;
}
