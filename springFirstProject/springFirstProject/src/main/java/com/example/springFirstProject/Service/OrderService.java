package com.example.springFirstProject.Service;

import com.example.springFirstProject.Beans.*;
import com.example.springFirstProject.Beans.Exceptions.BusinessException;
import com.example.springFirstProject.Beans.Exceptions.ResourceNotFoundException;
import com.example.springFirstProject.DTO.orderdto.OrderItemsDTO;
import com.example.springFirstProject.DTO.orderdto.OrderResponse;
import com.example.springFirstProject.Enums.OrderStatus;
import com.example.springFirstProject.Enums.PaymentStatus;
import com.example.springFirstProject.repository.CartItemRepository;
import com.example.springFirstProject.repository.OrderRepository;
import com.example.springFirstProject.repository.ProductRepository;
import com.example.springFirstProject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/*  Previously Written Code including all the Business login in one method


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public ResponseEntity<?> placeOrder(String userId) {
        // Validate the User
        // Validate the Product Item and Validate the valid product Quantity
        // Place the order by validating Payment Status and Order Status
        // Remove all Ordered Items from the Cart once its successfully Ordered
        Optional<User> user = userRepository.findById(Long.valueOf(userId));

        if (user.isEmpty()) {
            return new ResponseEntity<>("User not Exists or Not Login Yet!!!", HttpStatusCode.valueOf(404));
        }

        User existingUser = user.get();

        List<CartItem> cartItems = cartItemRepository.getCartItem(Long.valueOf(userId));

        if (cartItems.isEmpty()) {
            return new ResponseEntity<>("There is no Cart Items in the Bucket List, Please shop the Items...", HttpStatusCode.valueOf(403));
        } else {
            for (CartItem cartItem : cartItems) {
                Optional<Product> existedProduct = productRepository.findByproductIdAndActiveTrue(cartItem.getProduct().getProductId());

                if(existedProduct.isEmpty()) {
                    return new ResponseEntity<>(cartItem.getProduct().getProductName() + " Product Not Found, Please try Again!!!" , HttpStatusCode.valueOf(404));
                }

                if (existedProduct.get().getProductStockQuantity() < cartItem.getProductQuantity()){
                    return new ResponseEntity<>(cartItem.getProduct().getProductName() + " Is Out Of Stock OR Insufficient Product Quantity!!!", HttpStatusCode.valueOf(404));
                }
            }
        }

        Order newOrder = new Order();
        newOrder.setUser(existingUser);

        cartItems.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setProductQuantity(cartItem.getProductQuantity());

            orderItem.setTotalPrice(orderItem.calculatedTotalAmount());

            newOrder.addOrderItem(orderItem);
        });

        newOrder.setOrderStatus(OrderStatus.CONFIRMED);
        newOrder.setPaymentStatus(PaymentStatus.PAID);
        newOrder.setDiscount(BigDecimal.TEN);
        newOrder.setShippingCharge(BigDecimal.valueOf(20.20));
        newOrder.setTaxAmount(BigDecimal.valueOf(12.5));
        newOrder.calculateTotalAmount();
        Order savedOrder = orderRepository.save(newOrder);

        cartItems.forEach(cartItem -> {
            Product product = cartItem.getProduct();
            product.setProductStockQuantity(product.getProductStockQuantity() - cartItem.getProductQuantity());
            if (product.getProductStockQuantity() <= 0) product.setActive(false);
            productRepository.save(product);
        });


        if (newOrder.getOrderStatus() == OrderStatus.CONFIRMED) {
            cartItemRepository.deleteAll(cartItems);
        }
        return new ResponseEntity<>(mapToOrderResponse(savedOrder), HttpStatusCode.valueOf(200));

    }

    public OrderResponse mapToOrderResponse (Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderInvoice(order.getOrderInvoice());
        orderResponse.setTotalAmount(order.getTotalAmount());
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setCreatedAt(order.getCreatedAt());
        List<OrderItemsDTO> orderItemsDTOList = order
                .getOrderItems()
                .stream()
                        .map(orderItem -> {
                            OrderItemsDTO orderItemsDTO = new OrderItemsDTO();
                            orderItemsDTO.setProductName(orderItem.getProduct().getProductName());
                            orderItemsDTO.setProductQuantity(orderItem.getProductQuantity());
                            orderItemsDTO.setTotalPrice(orderItem.getTotalPrice());
                            return orderItemsDTO;
                        }
                        ).toList();
        orderResponse.setOrderItemsDTOList(orderItemsDTOList);
        return orderResponse;
    }
}

 */

//  *********** Optimized code for the above version ***********


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public OrderResponse placeOrder(Long userId) {
        User user = validateUser(userId);
        List<CartItem> cartItems = validateCart(userId);
        validateProducts(cartItems);

        Order newOrder = buildOrder(user, cartItems);
        Order savedOrder = orderRepository.save(newOrder);

        updateProductStock(cartItems);
        clearCart(cartItems, newOrder);

        return mapToOrderResponse(savedOrder);
    }

    // ---------- VALIDATIONS ---------- //

    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found or not logged in."));
    }

    private List<CartItem> validateCart(Long userId) {
        List<CartItem> cartItems = cartItemRepository.getCartItem(userId);
        if (cartItems.isEmpty()) {
            throw new BusinessException("Cart is empty. Please add items before placing an order.");
        }
        return cartItems;
    }

    private void validateProducts(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findByproductIdAndActiveTrue(cartItem.getProduct().getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            cartItem.getProduct().getProductName() + " not found or inactive."
                    ));

            if (product.getProductStockQuantity() < cartItem.getProductQuantity()) {
                throw new BusinessException(product.getProductName() + " is out of stock or insufficient quantity.");
            }
        }
    }

    // ---------- BUSINESS LOGIC ---------- //

    private Order buildOrder(User user, List<CartItem> cartItems) {
        Order order = new Order();
        order.setUser(user);

        cartItems.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setProductQuantity(cartItem.getProductQuantity());
            orderItem.setTotalPrice(orderItem.calculatedTotalAmount());
            order.addOrderItem(orderItem);
        });

        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setDiscount(BigDecimal.TEN);
        order.setShippingCharge(BigDecimal.valueOf(20.20));
        order.setTaxAmount(BigDecimal.valueOf(12.5));
        order.calculateTotalAmount();

        return order;
    }

    private void updateProductStock(List<CartItem> cartItems) {
        cartItems.forEach(cartItem -> {
            Product product = cartItem.getProduct();
            product.setProductStockQuantity(product.getProductStockQuantity() - cartItem.getProductQuantity());
            if (product.getProductStockQuantity() <= 0) {
                product.setActive(false);
            }
            productRepository.save(product);
        });
    }

    private void clearCart(List<CartItem> cartItems, Order order) {
        if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
            cartItemRepository.deleteAll(cartItems);
        }
    }

    // ---------- DTO MAPPER ---------- //

    public OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemsDTO> orderItemsDTOList = order.getOrderItems().stream()
                .map(orderItem -> new OrderItemsDTO(
                        orderItem.getProduct().getProductName(),
                        orderItem.getProductQuantity(),
                        orderItem.getTotalPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getOrderInvoice(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                orderItemsDTOList,
                order.getCreatedAt()
        );
    }
}

