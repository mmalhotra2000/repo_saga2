package com.assignment.ordermanagement.orderservice.services.commands;

import java.util.concurrent.CompletableFuture;

import com.assignment.ordermanagement.orderservice.dto.commands.OrderCreateDTO;

public interface OrderCommandService {

    public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO);

}
