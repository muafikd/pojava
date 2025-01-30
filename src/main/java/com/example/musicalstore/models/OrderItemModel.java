package com.example.musicalstore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private OrderModel order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductModel product;

    private int quantity;
    private double price;
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public void setOrder(OrderModel order) {
        this.order = order;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public OrderModel getOrder() {
        return order;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public ProductModel getProduct() {
        return product;
    }
}
