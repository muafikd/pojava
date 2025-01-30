package com.example.musicalstore.repositories;

import com.example.musicalstore.models.OrderModel;
import com.example.musicalstore.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderModel, Long> {
    List<OrderModel> findByUser(UserModel user);
    List<OrderModel> findByUserOrderByPlacedAtDesc(UserModel user);
}