package com.example.demo.dto;

public record OrderResponse(
        Long id,
        String productName,
        Double price,
        String status) {
}
