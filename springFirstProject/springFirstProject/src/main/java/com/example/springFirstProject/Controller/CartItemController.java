package com.example.springFirstProject.Controller;

import com.example.springFirstProject.DTO.cartdto.CartItemRequest;
import com.example.springFirstProject.Service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestHeader("User-X-ID") String userId, @RequestBody CartItemRequest cartItemRequest) {
        System.out.println(userId);
        return cartItemService.addToCart(userId, cartItemRequest);
    }

    @GetMapping
    public ResponseEntity<?> getAllCartByUserId(@RequestHeader("User-X-ID") String userId) {
        return cartItemService.getAllCartByUserId(userId);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteCartItem(@RequestHeader("User-X-ID") String userId, @PathVariable Long productId) {
        return cartItemService.deleteCartItem(userId, productId);
    }
}
