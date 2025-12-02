package com.example.demo.listener;

import com.example.demo.event.OrderCreatedEvent;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderListener {

    private final OrderService orderService;

    @KafkaListener(topics = "orders_topic", groupId = "order-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("Received OrderCreatedEvent for order: " + event.orderId());
        orderService.processOrder(event.orderId());
    }
}
