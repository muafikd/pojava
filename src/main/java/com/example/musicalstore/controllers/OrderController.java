package com.example.musicalstore.controllers;

import com.example.musicalstore.models.OrderModel;
import com.example.musicalstore.models.UserModel;
import com.example.musicalstore.services.OrderService;
import com.example.musicalstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder() {
        try {
            UserModel currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }

            OrderModel order = orderService.createOrder();

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getOrderId());
            response.put("totalPrice", order.getTotalPrice());
            response.put("status", order.getOrderStatus());
            response.put("placedAt", order.getPlacedAt());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> getUserOrders() {
        try {
            List<OrderModel> orders = orderService.getUserOrders();
            List<Map<String, Object>> response = orders.stream()
                    .map(order -> {
                        Map<String, Object> orderMap = new HashMap<>();
                        orderMap.put("orderId", order.getOrderId());
                        orderMap.put("totalPrice", order.getTotalPrice());
                        orderMap.put("orderStatus", order.getOrderStatus());
                        orderMap.put("placedAt", order.getPlacedAt());
                        return orderMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId) {
        try {
            OrderModel order = orderService.getOrderById(orderId);
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getOrderId());
            response.put("totalPrice", order.getTotalPrice());
            response.put("orderStatus", order.getOrderStatus());
            response.put("placedAt", order.getPlacedAt());

            List<Map<String, Object>> items = order.getOrderItems().stream()
                    .map(item -> {
                        Map<String, Object> itemMap = new HashMap<>();
                        itemMap.put("productName", item.getProduct().getTitle());
                        itemMap.put("quantity", item.getQuantity());
                        itemMap.put("price", item.getPrice());
                        return itemMap;
                    })
                    .collect(Collectors.toList());

            response.put("items", items);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        try {
            UserModel currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }

            // Здесь можно добавить проверку роли пользователя,
            // если статус могут менять только админы

            OrderModel updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating order status: " + e.getMessage());
        }
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<?> getOrderItems(@PathVariable Long orderId) {
        try {
            UserModel currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.badRequest().body("User not authenticated");
            }

            OrderModel order = orderService.getOrderById(orderId);

            // Проверяем, принадлежит ли заказ текущему пользователю
            if (!order.getUser().getUserId().equals(currentUser.getUserId())) {
                return ResponseEntity.badRequest().body("Order does not belong to current user");
            }

            return ResponseEntity.ok(order.getOrderItems());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching order items: " + e.getMessage());
        }
    }
}