package com.example.musicalstore.repositories;

import com.example.musicalstore.models.OrderItemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemModel, Long> {
}