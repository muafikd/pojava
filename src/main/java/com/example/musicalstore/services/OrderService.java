package com.example.musicalstore.services;

import com.example.musicalstore.models.*;
import com.example.musicalstore.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public OrderModel createOrder() {
        try {
            // Получаем текущего пользователя
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserModel user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Получаем товары из корзины
            List<CartItemModel> cartItems = cartService.viewCart();

            if (cartItems.isEmpty()) {
                throw new RuntimeException("Cart is empty");
            }

            // Вычисляем общую стоимость
            double totalPrice = cartItems.stream()
                    .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                    .sum();

            // Создаем заказ
            OrderModel order = new OrderModel();
            order.setUser(user);
            order.setTotalPrice(totalPrice);
            order.setOrderStatus("PENDING");
            order.setPlacedAt(java.time.LocalDateTime.now());
            order.setOrderItems(new ArrayList<>());

            // Сохраняем заказ
            order = orderRepository.save(order);

            // Создаем элементы заказа
            List<OrderItemModel> orderItems = new ArrayList<>();
            for (CartItemModel cartItem : cartItems) {
                OrderItemModel orderItem = new OrderItemModel();
                orderItem.setOrder(order);
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(cartItem.getProduct().getPrice());
                orderItems.add(orderItem);
            }

            // Сохраняем элементы заказа
            orderItemRepository.saveAll(orderItems);
            order.setOrderItems(orderItems);

            // Очищаем корзину
            cartService.clearCart();

            return order;
        } catch (Exception e) {
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }

    public List<OrderModel> getUserOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByPlacedAtDesc(user);
    }

    public OrderModel getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public OrderModel updateOrderStatus(Long orderId, String newStatus) {
        OrderModel order = getOrderById(orderId);
        order.setOrderStatus(newStatus);
        return orderRepository.save(order);
    }
}