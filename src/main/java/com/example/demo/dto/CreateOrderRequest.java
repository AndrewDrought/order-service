package com.example.demo.dto;

public record CreateOrderRequest(
        String productName,
        Double price) {
}
