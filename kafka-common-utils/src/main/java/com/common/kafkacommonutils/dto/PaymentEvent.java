package com.common.kafkacommonutils.dto;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent {
    private String transactionId;
    private double amount;
    private String status;
}