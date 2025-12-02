package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.event.OrderCreatedEvent;
import com.example.demo.dto.OrderResponse;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public OrderResponse createOrder(com.example.demo.dto.CreateOrderRequest request) {
        Order order = new Order();
        order.setProductName(request.productName());
        order.setPrice(request.price());
        order.setStatus("NEW");

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getProductName(),
                savedOrder.getPrice());

        try {
            String payload = objectMapper.writeValueAsString(event);

            com.example.demo.entity.Outbox outbox = com.example.demo.entity.Outbox.builder()
                    .topic("orders_topic")
                    .payload(payload)
                    .status("PENDING")
                    .createdAt(java.time.LocalDateTime.now())
                    .build();

            outboxRepository.save(outbox);

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getProductName(),
                savedOrder.getPrice(),
                savedOrder.getStatus());
    }

    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getProductName(),
                        order.getPrice(),
                        order.getStatus()))
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Transactional
    public void processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("PROCESSED");
        orderRepository.save(order);
        System.out.println("Order processed: " + orderId);
    }
}
