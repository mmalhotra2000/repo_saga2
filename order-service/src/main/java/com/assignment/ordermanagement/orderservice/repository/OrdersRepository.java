package com.assignment.ordermanagement.orderservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.ordermanagement.orderservice.entity.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {

}
