package com.silverbars.marketplace.orders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class Order {

    private String userId;
    private double quantity; //Assume for example basic precision is acceptable would review against usage in real world
    private int pricePerKilo; //Assume that this is always in whole pounds as per the examples
    private OrderType orderType;

    public Order(String userId, double quantity, int pricePerKilo, OrderType orderType) {
        checkNotNull(userId, "UserId must be supplied but was null");
        checkArgument(quantity > 0, "Quantity was %s but expected greater than zero", quantity);
        checkArgument(pricePerKilo > 0, "PricePerKilo was %s but expected greater than zero", pricePerKilo);
        checkNotNull(orderType, "OrderType must be supplied but was null");
        this.userId = userId;
        this.quantity = quantity;
        this.pricePerKilo = pricePerKilo;
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getPricePerKilo() {
        return pricePerKilo;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
