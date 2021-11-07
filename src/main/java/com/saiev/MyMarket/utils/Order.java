package com.saiev.MyMarket.utils;

import com.saiev.MyMarket.entities.OrderItem;
import com.saiev.MyMarket.entities.User;
import lombok.Data;

import java.util.List;

@Data
public class Order {

    private User user;
    private List<OrderItem> orderItems;
    private double price;

    public Order(User user, List<OrderItem> orderItems, double price) {
        this.user = user;
        this.orderItems = orderItems;
        this.price = price;
    }

    public Order() {
    }
}