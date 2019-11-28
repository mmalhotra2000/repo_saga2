package com.assignment.ordermanagement.orderservice.aggregates;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import com.assignment.ecommerce.commands.CreateOrderCommand;
import com.assignment.ecommerce.commands.UpdateOrderStatusCommand;
import com.assignment.ecommerce.events.OrderCreatedEvent;
import com.assignment.ecommerce.events.OrderUpdatedEvent;
import com.assignment.ordermanagement.orderservice.entity.Orders;
import com.assignment.ordermanagement.orderservice.repository.OrdersRepository;

@Aggregate
public class OrderAggregate {

	@AggregateIdentifier
	private String orderId;

	private ItemType itemType;

	private BigDecimal price;

	private String currency;

	private OrderStatus orderStatus;

	@Autowired
	private OrdersRepository ordersRepository;

	public OrderAggregate() {
	}

	@CommandHandler
	public OrderAggregate(CreateOrderCommand createOrderCommand) {

		AggregateLifecycle.apply(new OrderCreatedEvent(createOrderCommand.orderId, createOrderCommand.itemType,
				createOrderCommand.price, createOrderCommand.currency, createOrderCommand.orderStatus));

	}

	@EventSourcingHandler
	protected void on(OrderCreatedEvent orderCreatedEvent) {
		this.orderId = orderCreatedEvent.orderId;
		this.itemType = ItemType.valueOf(orderCreatedEvent.itemType);
		this.price = orderCreatedEvent.price;
		this.currency = orderCreatedEvent.currency;
		this.orderStatus = OrderStatus.valueOf(orderCreatedEvent.orderStatus);
		Orders order1 = new Orders(orderCreatedEvent.orderId, orderCreatedEvent.itemType, orderCreatedEvent.price,
				orderCreatedEvent.currency, orderCreatedEvent.orderStatus);
		System.out.println(order1);
		ordersRepository.save(order1);
	}

	@CommandHandler
	protected void on(UpdateOrderStatusCommand updateOrderStatusCommand) {
		AggregateLifecycle
				.apply(new OrderUpdatedEvent(updateOrderStatusCommand.orderId, updateOrderStatusCommand.orderStatus));
	}

	@EventSourcingHandler
	protected void on(OrderUpdatedEvent orderUpdatedEvent) {
		this.orderId = orderUpdatedEvent.orderId;
		this.orderStatus = OrderStatus.valueOf(orderUpdatedEvent.orderStatus);
		Orders orders = ordersRepository.findById(orderId).get();
		System.out.println("orders in orderUpdatedEvent :: "+orders);
		if (null != orders) {
			orders.setOrderStatus(orderUpdatedEvent.orderStatus);
			ordersRepository.save(orders);
		}
	}
}
