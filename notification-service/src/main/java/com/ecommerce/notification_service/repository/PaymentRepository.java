package com.ecommerce.notification_service.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ecommerce.notification_service.entity.Payment;

@Repository
public class PaymentRepository extends MongoRepository<Payment, String> {
    
}
