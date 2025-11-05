package com.example.springFirstProject.Service;

import com.example.springFirstProject.Beans.CartItem;
import com.example.springFirstProject.Beans.Product;
import com.example.springFirstProject.Beans.User;
import com.example.springFirstProject.DTO.cartdto.CartItemRequest;
import com.example.springFirstProject.DTO.cartdto.CartItemResponse;
import com.example.springFirstProject.repository.CartItemRepository;
import com.example.springFirstProject.repository.ProductRepository;
import com.example.springFirstProject.repository.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemService(UserRepository userRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public ResponseEntity<?> addToCart(String userId, CartItemRequest cartItemRequest) {
        Optional<Product> existingProduct = productRepository.findByproductIdAndActiveTrue(cartItemRequest.getProductId());
        if (existingProduct.isEmpty()) {
            return new ResponseEntity<>("Product Not Present!!!", HttpStatusCode.valueOf(403));
        }

        if (existingProduct.get().getProductStockQuantity() < cartItemRequest.getProductQuantity()){
            return new ResponseEntity<>("Insufficient Products!!!", HttpStatusCode.valueOf(403));
        }

        Optional<User> existingUser = userRepository.findById(Long.valueOf(userId));
        if (existingUser.isEmpty()) {
            return new ResponseEntity<>("User not Exists or Not Login Yet!!!", HttpStatusCode.valueOf(405));
        }
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct(existingUser.get(), existingProduct.get());
        if (existingCartItem.isPresent()) {
            // Update the Existing cart by adding the updated quantity and price.
            CartItem updateCartItem = existingCartItem.get();
            updateCartItem.setProductQuantity(updateCartItem.getProductQuantity() + cartItemRequest.getProductQuantity());
            updateCartItem.setProductPrice(updateCartItem.getProduct().getProductPrice()
                    .multiply(BigDecimal.valueOf(updateCartItem.getProductQuantity())));
            cartItemRepository.save(updateCartItem);
            return new ResponseEntity<>(mapToCartResponse(updateCartItem), HttpStatusCode.valueOf(200));
        } else {
            // Add new Item to cart.
            CartItem addNewItem = new CartItem();
            addNewItem.setUser(existingUser.get());
            addNewItem.setProduct(existingProduct.get());
            addNewItem.setProductQuantity(cartItemRequest.getProductQuantity());
            addNewItem.setProductPrice(existingProduct.get().getProductPrice()
                    .multiply(BigDecimal.valueOf(cartItemRequest.getProductQuantity())));
            cartItemRepository.save(addNewItem);
            return new ResponseEntity<>(mapToCartResponse(addNewItem), HttpStatusCode.valueOf(201));
        }
    }

    public ResponseEntity<?> getAllCartByUserId(String userId) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();

        cartItemRepository.findByUserId(Long.valueOf(userId))
                .stream()
                .map(cartItem -> {
                    return cartItemResponses.add(mapToCartResponse(cartItem));
                }).toList();
        return new ResponseEntity<>(cartItemResponses, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<?> deleteCartItem(String userId, Long productId) {
        Optional<Product> existingProduct = productRepository.findByproductIdAndActiveTrue(productId);
        if (existingProduct.isEmpty()) {
            return new ResponseEntity<>("Product Not Present!!!", HttpStatusCode.valueOf(403));
        }

        Optional<User> existingUser = userRepository.findById(Long.valueOf(userId));
        if (existingUser.isEmpty()) {
            return new ResponseEntity<>("User not Exists or Not Login Yet!!!", HttpStatusCode.valueOf(405));
        }
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct(existingUser.get(), existingProduct.get());
        if (existingCartItem.isPresent()) {
            cartItemRepository.delete(existingCartItem.get());
            return new ResponseEntity<>(mapToCartResponse(existingCartItem.get()), HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>("Cart doesn't Existed or Deleted from the cart List!!!", HttpStatusCode.valueOf(200));
        }
    }

    public CartItemResponse mapToCartResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setProductName(cartItem.getProduct().getProductName());
        cartItemResponse.setProductDescription(cartItem.getProduct().getProductDescription());
        cartItemResponse.setProductCategory(cartItem.getProduct().getProductCategory());
        cartItemResponse.setProductQuantity(cartItem.getProductQuantity());
        cartItemResponse.setProductPrice(cartItem.getProductPrice());
        return cartItemResponse;
    }
}
