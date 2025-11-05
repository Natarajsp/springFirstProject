package com.example.springFirstProject.Controller;

import com.example.springFirstProject.DTO.orderdto.OrderResponse;
import com.example.springFirstProject.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("User-X-ID") String userId) {
        Long userID = Long.valueOf(userId);
        OrderResponse response = orderService.placeOrder(userID);
        return ResponseEntity.ok(response);
    }
}
