package com.example.demo.event;

public record OrderCreatedEvent(
        Long orderId,
        String productName,
        Double price) {
}
